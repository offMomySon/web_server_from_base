package response.sender;

import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;
import response.messageFactory.creater.OrderedMessageResponserFactories;
import thread.ThreadTasker;
import thread.snapshot.ThreadStatusSnapShot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static reader.httpspec.startLine.HttpStartLine.EMPTY_REQUEST_TARGET;

@Slf4j
public class ThreadRelatedRequestSender extends RequestSender {
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void doProcess(String hostAddress, HttpRequest httpRequest) {
        ThreadTasker threadTasker = new ThreadTasker();
        ThreadStatusSnapShot statusSnapShot = threadTasker.createStatusSnapShot();
        AbstractMessageFactory responserFactory = new OrderedMessageResponserFactories(statusSnapShot).create();

        //ditto
        // 검사 -> increase wait count 하기 전까지 동기화 해야함.
        // 아니면 2 개의 thread 가 검사를 통과하고 wait count 가 2개 이상 증가 할 수 있음.
        if (!statusSnapShot.isAvailable()) {
            Message message = responserFactory.createMessage("", EMPTY_REQUEST_TARGET);
            message.create();
            return;
        }

        // 문제.
        // 이 구문 전체가 동기화 보장되어야 할 거 같다.
        // 왜냐하면 지금은 thread 각각의 count 증감만 동기화 되어있기 때문에 아래 sequence 에서
        // 설정한 useable thread count 보다 더 많은 thread 가 message 생성에 사용 될 수 있다.
        // usableWorker 의 count 가 하나 감소했을 때, waitableWorker 에서 waiting 하고 있던 2개 이상의 thread 가 동시에
        // 빠져나올수 있음. 그러면 설정한 usable thread count 보다 더많은 request 를 처리하게됨.

        // 해결방안.
        // wait notify 또는 condition 으로 바꿔야 할듯.
        // 1) check run count -> decrease wait count -> increase run count 동기화 보장되어야함.
        // 2) check run count -> increase run count 동기화 보장되어야함.
        threadPool.execute(() -> {
            threadTasker.run(() -> {
                Message message = responserFactory.createMessage(hostAddress, httpRequest.getHttpStartLine().getResourcePath());
                message.create();
            });
        });
    }
}