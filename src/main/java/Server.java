import action.*;
import config.ConfigManager;
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
import util.TriFunction;

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

    public static void sendMessage(Socket socket, Message message) {
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

    private static ThreadTask buildThreadTask(ThreadTaskType type,
                                              Function<PreTask, Runnable> preTaskCreator,
                                              PreTask preTask,
                                              Function<AbstractMessageFactory, Runnable> runnableCreator,
                                              AbstractMessageFactory threadMessageFactory) throws IOException {
        return ThreadTask
                .empty(type)
                .andThen(preTaskCreator.apply(preTask))
                .andThen(runnableCreator.apply(threadMessageFactory));
    }

    public void start() {
        //TODO 어셈블러 장점 이해하기
        ThreadTasker threadTasker = new ThreadTasker();

        WelcomeMessageFactory welcomeMessageFactory = new WelcomeMessageFactory();
        DirectoryMessageFactory directoryMessageFactory = new DirectoryMessageFactory();
        Function<String, AbstractMessageFactory> mainThreadFactoryCreator = (hostAddress) -> new CompositeMessageFactory(List.of(
                welcomeMessageFactory,
                directoryMessageFactory,
                new FilteredMessageFactory(hostAddress),
                new ThreadNotExistMessageFactory(threadTasker.createStatusSnapShot()),
                new ExceedDownloadCountMessageFactory(hostAddress)
        ));

        FileMessageFactory fileMessageFactory = new FileMessageFactory();
        AbstractMessageFactory workerThreadMessageFactory = new CompositeMessageFactory(List.of(
                fileMessageFactory
        ));

        BiFunction<String, ResourcePath, PreTask> preTaskCreator = (hostAddress, resourcePath) -> new CompositedPreTask(List.of(
                RestrictPreTask.create(hostAddress, resourcePath.createFileExtension())
        ));

        TriFunction<PreTask, AbstractMessageFactory, Socket, ActionCreator> actionCreator = (preTask, messageFactory, socket) -> new CompositedActionCreator(List.of(
                new PreTaskActionCreator(preTask),
                MessageActionCreator.create(messageFactory, socket))
        );

        //TODO 어셈블러 장점 이해하기 END
        Socket socket = UNBOUNDED;

        try {
            while (true) {
                socket = getSocket();

                String hostAddress = getHostAddress(socket);

                // TODO 구조 이해하기 (노션에다가 정리해서 설명) HINT. PRE MESSAGE TASK -> MAin MESSAGE TASK -> VIEW RENDERING (예시)
                // TODO 구조를 이해했으면, 그 구조가 코드로 표현할 수 있게 다듬기 (위 구조를 잡았으면 위 구조대로 해석할 수 있게 코드로 표현하기
                // TODO 아래코드는 구조는 잡혔으나 시스템 표현이 안좋다. 그렇기에 뭉탱이로 처리할것들을 처리하여 위 구조가 '잘' 드러나도록 코드를 리팩토링하기
                // TODO 이 과정에서 특정 class 파일이 나올것이다. 해당 파일의 역할을 생각하여 구조를 다듬어 보자.
                ResourcePath resourcePath = new HttpRequest(socket.getInputStream()).getHttpStartLine().getResourcePath();
//
////                Function<PreTask, Runnable> messagePreTaskCreator = createMessagePreTaskCreator(resourcePath);
//                Function<AbstractMessageFactory, Runnable> messageHandlerCreator = getRunnableCreator(socket, resourcePath);
//
//                PreTask preTask = preTaskCreator.apply(hostAddress, resourcePath);
//                AbstractMessageFactory mainFactory = mainThreadFactoryCreator.apply(hostAddress);
//                ThreadTaskType taskType = mainFactory.isSupported(resourcePath) ? ThreadTaskType.MAIN : ThreadTaskType.THREAD;
//                AbstractMessageFactory targetFactory = taskType.isMain() ? mainFactory : workerThreadMessageFactory;

//                threadTasker.run(buildThreadTask(taskType, messagePreTaskCreator, preTask, messageHandlerCreator, targetFactory));

                TaskCreator taskCreator = TaskCreator.create(socket, hostAddress,
                        preTaskCreator.apply(hostAddress, resourcePath),
                        resourcePath,
                        mainThreadFactoryCreator.apply(hostAddress),
                        workerThreadMessageFactory,
                        actionCreator);

                ThreadTask threadTask = taskCreator.create();
                threadTasker.run(threadTask);


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

    @NotNull
    private Function<AbstractMessageFactory, Runnable> getRunnableCreator(Socket socket, ResourcePath resourcePath) {
        return (messageFactory) -> {
            Message message = messageFactory.createMessage(resourcePath);

            return () -> Server.sendMessage(socket, message);
        };
    }

}
