import config.ConfigManager;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sender.factory.AbstractMessageResponserFactory;
import sender.factory.OrderedMessageResponserFactories;

public class Server {

  private static final Socket UNBOUNDED = null;
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ServerSocket serverSocket;
  private final ThreadController threadController;
  private final ConfigManager configManager;
  private final AbstractMessageResponserFactory abstractMessageResponserFactory;

  public Server(ConfigManager configManager) {
    this.configManager = configManager;
    serverSocket = createServerSocket(configManager.getPort());
    threadController = createThreadController(configManager);
    abstractMessageResponserFactory = new OrderedMessageResponserFactories(threadController, configManager).create();
  }

  private static ServerSocket createServerSocket(int port) {
    try {
      return new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static ThreadController createThreadController(ConfigManager configManager) {
    return new ThreadController(configManager.getThreadConfig());
  }

  public void start() {
    Socket socket = UNBOUNDED;
    try {
      while (true) {
        logger.info("waiting.. request");
        socket = serverSocket.accept();

        logger.info("accept.. request");
        logger.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress(), socket.getPort());

        threadController.process(socket, configManager, abstractMessageResponserFactory);

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
