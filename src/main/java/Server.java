import config.ConfigManager;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.message.sender.Message;
import response.messageFactory.*;
import thread.ThreadTask;
import thread.ThreadTaskType;
import thread.ThreadTasker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public void start() {
        ThreadTasker threadTasker = new ThreadTasker();

        Supplier<AbstractMessageFactory> mainThreadFactoryCreator = () -> new CompositeMessageFactory(List.of(
                new WelcomeMessageFactory(),
                new DirectoryMessageFactory(),
                new FilteredMessageFactory(),
                new ThreadNotExistMessageFactory(threadTasker.createStatusSnapShot()),
                new ExceedDownloadCountMessageFactory()
        ));

        AbstractMessageFactory workerThreadMessageFactory = new CompositeMessageFactory(List.of(
                new WelcomeMessageFactory(),
                new FileMessageFactory()
        ));

        Socket socket = UNBOUNDED;

        try {
            while (true) {
                log.info("waiting.. request");
                socket = serverSocket.accept();

                log.info("accept.. request");
                log.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress().getHostAddress(), socket.getPort());

                ThreadTask threadTask = createThreadTask(socket, mainThreadFactoryCreator.get(), workerThreadMessageFactory);

                if (Objects.nonNull(threadTask)) {
                    threadTasker.run(threadTask);
                }

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

    private ThreadTask createThreadTask(Socket socket, AbstractMessageFactory mainThreadMessageFactory, AbstractMessageFactory workerThreadMessageFactory) throws IOException {
        String hostAddress = socket.getInetAddress().getHostAddress();
        ResourcePath resourcePath = new HttpRequest(socket.getInputStream()).getHttpStartLine().getResourcePath();

        if (resourcePath.getValue().toString().equals("/favicon.ico")) {
            log.info("return favicon");
            return null;
        }

        // 더 좋은 이름 없을까.
        // thread 관점이라서 runnable 이 좋아보이기는 하는데..
        Function<AbstractMessageFactory, Runnable> runnableCreator = (messageFactory) -> {
            Message message = messageFactory.createMessage(hostAddress, resourcePath);
            return () -> sendMessage(socket, message);
        };

        if (mainThreadMessageFactory.isSupported(hostAddress, resourcePath)) {
            return new ThreadTask(ThreadTaskType.MAIN, runnableCreator.apply(mainThreadMessageFactory));
        }
        
        return new ThreadTask(ThreadTaskType.THREAD, runnableCreator.apply(workerThreadMessageFactory));
    }

}
