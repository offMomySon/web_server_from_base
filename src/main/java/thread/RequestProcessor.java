package thread;

import config.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import reader.HttpRequest;
import sender.factory.AbstractMessageResponserFactory;
import sender.factory.OrderedMessageResponserFactories;
import sender.strategy.MessageResponser;

public class RequestProcessor {
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private final AbstractMessageResponserFactory responserFactory;

  public RequestProcessor() {
    responserFactory = new OrderedMessageResponserFactories(ConfigManager.getInstance()).create();
  }

  // 안티패턴?
  // 어차피 실제 실행되는 메소느드는 doProcess() 니까 성문 여러개 만들어도 괜찮지 않나?
  // 인자받기도 쉬운거 같은데
//  public void process(Socket socket) {
//    try (InputStream inputStream = socket.getInputStream();
//        OutputStream outputStream = socket.getOutputStream();) {
//      doProcess(inputStream, outputStream);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  public void doProcess(InputStream inputStream, OutputStream outputStream) {
    ThreadResourceManager threadResourceManager = new ThreadResourceManager();

    //ditto
    // 검사 -> increase wait count 하기 전까지 동기화 해야함.
    // 아니면 2 개의 thread 가 검사를 통과하고 wait count 가 2개 이상 증가 할 수 있음.
    if (!threadResourceManager.isProcessable()) {
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
      threadResourceManager.runProcessIn(() -> doSend(inputStream, outputStream));
    });
  }

  private void doSend(InputStream inputStream, OutputStream outputStream) {
    String requestTarget = new HttpRequest(inputStream).getRequestTarget();

    MessageResponser messageResponser = responserFactory.createMessageResponser(requestTarget);
    messageResponser.doSend(outputStream);
  }
}
