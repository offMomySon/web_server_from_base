import config.ConfigManager;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.message.sender.Message;
import response.messageFactory.*;
import thread.ThreadTasker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
                new ThreadNotExistMessageFactory(threadTasker.createStatusSnapShot())
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

                String hostAddress = socket.getInetAddress().getHostAddress();
                ResourcePath resourcePath = new HttpRequest(socket.getInputStream()).getHttpStartLine().getResourcePath();

                AbstractMessageFactory mainThreadFactory = mainThreadFactoryCreator.get();
                if (mainThreadFactory.isSupported(hostAddress, resourcePath)) {

                    Message message = mainThreadFactory.createMessage(hostAddress, resourcePath);
                    sendMessage(socket, message);
                    socket = UNBOUNDED;
                    continue;
                }

                Socket _socket = socket;
                threadTasker.run(() -> {
                    Message message = workerThreadMessageFactory.createMessage(hostAddress, resourcePath);
                    sendMessage(_socket, message);
                });

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

}
