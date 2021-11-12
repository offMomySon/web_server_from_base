import config.ConfigManager;
import resource.ResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Server {

  private static final Socket UNBOUNDED = null;
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ResourceController resourceController;
  private final ConfigManager configManager;
  private final ServerSocket serverSocket;
  private final ExecutorService executorService;

  public Server(ConfigManager configManager) {
    this.configManager = configManager;

    resourceController = createResourceManager(configManager.getDownloadPath());
    serverSocket = createServerSocket(configManager.getPort());

    executorService = ForkJoinPool.commonPool();
  }

  private static ServerSocket createServerSocket(int port) {
    try {
      return new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static ResourceController createResourceManager(String resourceRootPath) {
    return new ResourceController(resourceRootPath);
  }

  public void start() {
    Socket socket = UNBOUNDED;
    try {
      while (true) {
        logger.info("waiting.. request");
        socket = serverSocket.accept();

        logger.info("accept.. request");
        logger.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress(),
            socket.getPort());

        executorService.submit(
            new Servlet(socket.getInputStream(), socket.getOutputStream(), resourceController));

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
        ioException.printStackTrace();
      }
    }
  }
}
