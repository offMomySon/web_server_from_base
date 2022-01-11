import Sequence.CompositedTaskRunner;
import Sequence.MainTaskRunner;
import Sequence.PreTaskRunner;
import Sequence.TaskRunner;
import config.ConfigManager;
import config.server.download.DownloadInfoRestrictChecker;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reader.httpspec.HttpRequest;
import response.message.sender.Message;
import response.messageFactory.*;
import response.pretask.CompositedPreTask;
import response.pretask.PreTask;
import response.pretask.RestrictPreTask;
import thread.ThreadTask;
import thread.ThreadTaskType;
import thread.ThreadTasker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class Server {
    private static final Socket UNBOUNDED = null;

    private final ServerSocket serverSocket;

    public Server(ConfigManager configManager) {
        serverSocket = createServerSocket(configManager.getBasicConfig().getPort());
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(Socket socket, Message message) {
        String content = message.create();

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getHostAddress(Socket socket) {
        String hostAddress = socket.getInetAddress().getHostAddress();
        log.info("New Client Connect! Connected IP : {}, Port : {}}", hostAddress, socket.getPort());

        return hostAddress;
    }

    @NotNull
    private static Function<AbstractMessageFactory, Runnable> getRunnableCreator(Socket socket, ResourcePath resourcePath) {
        return (messageFactory) -> {
            Message message = messageFactory.createMessage(resourcePath);

            return () -> sendMessage(socket, message);
        };
    }

    public void start() {
        //TODO 어셈블러 장점 이해하기
        // 비지니스 로직에 필요한 객체들을 미리 생성해서, 비지니스로직에만 집중할 수 있게 된다.
        // 객체들이 캐싱되기 때문에 서버의 메모리 사용량을 줄일 수 있다.

        Socket socket = UNBOUNDED;

        ThreadTasker threadTasker = new ThreadTasker();

        Function<String, AbstractMessageFactory> mainThreadFactoryCreator = (hostAddress) -> new CompositeMessageFactory(List.of(
                new WelcomeMessageFactory(),
                new DirectoryMessageFactory(),
                new FilteredMessageFactory(hostAddress),
                new ThreadNotExistMessageFactory(threadTasker.createStatusSnapShot()),
                new ExceedDownloadCountMessageFactory(hostAddress)
        ));

        AbstractMessageFactory workerThreadMessageFactory = new CompositeMessageFactory(List.of(
                new WelcomeMessageFactory(),
                new FileMessageFactory()
        ));

        BiFunction<String, ResourcePath, PreTask> preTaskCreator = (hostAddress, resourcePath) -> new CompositedPreTask(List.of(
                RestrictPreTask.create(hostAddress, resourcePath.createFileExtension())
        ));
        //TODO 어셈블러 장점 이해하기 END

        BiFunction<Socket, ResourcePath, TaskRunner> taskRunnerCreator = (_socket, resourcePath) -> new CompositedTaskRunner(List.of(
                new PreTaskRunner(preTaskCreator.apply(_socket.getInetAddress().getHostAddress(), resourcePath)),
                new MainTaskRunner(threadTasker,
                        mainThreadFactoryCreator.apply(getHostAddress(_socket)),
                        workerThreadMessageFactory,
                        getRunnableCreator(_socket, resourcePath)
                )
        ));

        try {
            while (true) {
                socket = getSocket();

                // TODO 구조 이해하기 (노션에다가 정리해서 설명) HINT. PRE MESSAGE TASK -> MAin MESSAGE TASK -> VIEW RENDERING (예시)
                // TODO 구조를 이해했으면, 그 구조가 코드로 표현할 수 있게 다듬기 (위 구조를 잡았으면 위 구조대로 해석할 수 있게 코드로 표현하기
                // TODO 아래코드는 구조는 잡혔으나 시스템 표현이 안좋다. 그렇기에 뭉탱이로 처리할것들을 처리하여 위 구조가 '잘' 드러나도록 코드를 리팩토링하기
                // TODO 이 과정에서 특정 class 파일이 나올것이다. 해당 파일의 역할을 생각하여 구조를 다듬어 보자.

                // 1. pre message task.
                // 2. main message task.
                ResourcePath resourcePath = new HttpRequest(socket.getInputStream()).getHttpStartLine().getResourcePath();

                TaskRunner taskRunner = taskRunnerCreator.apply(socket, resourcePath);
                taskRunner.doRun(resourcePath);

                socket = UNBOUNDED;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    private Socket getSocket() throws IOException {
        log.info("waiting.. request");
        Socket socket = serverSocket.accept();
        log.info("accept.. request");

        return socket;
    }
}
