import config.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.HttpRequest;
import sender.factory.AbstractMessageResponserFactory;
import sender.factory.OrderedMessageResponserFactories;
import sender.factory.ThreadMessageResponserFactory;
import sender.factory.thread.ThreadStatusSnapshot;
import sender.strategy.MessageResponser;

public class Server {

  private static final Socket UNBOUNDED = null;
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ExecutorService executorService = ForkJoinPool.commonPool();
  private final ServerSocket serverSocket;
  private final ThreadControllerSnapshot threadController;
  private final ConfigManager configManager;
  private final AbstractMessageResponserFactory abstractMessageResponserFactory;

  public Server(ConfigManager configManager) {
    this.configManager = configManager;

    serverSocket = createServerSocket(configManager.getBasicConfig().getPort());
    threadController = createThreadController(configManager);
    abstractMessageResponserFactory = new OrderedMessageResponserFactories(configManager).create();
  }

  private static ServerSocket createServerSocket(int port) {
    try {
      return new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static ThreadControllerSnapshot createThreadController(ConfigManager configManager) {
    return new ThreadControllerSnapshot(configManager.getThreadConfig());
  }

  public void start() {
    Socket socket = UNBOUNDED;
    try {
      while (true) {
        logger.info("waiting.. request");
        socket = serverSocket.accept();

        logger.info("accept.. request");
        logger.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress(), socket.getPort());

        process(socket);

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

  public void process(Socket socket) {
    try {
      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();

      process(inputStream, outputStream);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void process(InputStream inputStream, OutputStream outputStream) {
    if (!threadController.isProcessable()) {
      ThreadMessageResponserFactory noThreadCountFactory =
          new ThreadMessageResponserFactory(new ThreadStatusSnapshot() {
            @Override
            public boolean isAvailable() {
              return false;
            }
          });

      ThreadMessageResponserFactory factory2 = new ThreadMessageResponserFactory(() -> {
        return false;
      });

      ThreadMessageResponserFactory factory3 =
          new ThreadMessageResponserFactory(threadController.createSnapShot());

      MessageResponser messageResponser = factory3.createMessageResponser();

      messageResponser.doSend(outputStream);
//
//      AbstractMessageResponserFactory noThreadCountFactory = new ThreadMessageResponserFactory(threadController.createSnapShot());
//      doSend(inputStream, outputStream, noThreadCountFactory);

      return;
    }

    executorService.submit(() -> {
      threadController.waitIfNotExistUseableThread();

      threadController.runWithOccupiedWorkerThread(() -> doSend(inputStream, outputStream));
    });
  }

//  private static void doSend(InputStream inputStream, OutputStream outputStream,
//      AbstractMessageResponserFactory abstractMessageResponserFactory) {
//    String requestTarget = new HttpRequest(inputStream).getRequestTarget();
//
//    MessageResponser messageResponser = abstractMessageResponserFactory.createMessageResponser(requestTarget);
//    messageResponser.doSend(outputStream);
//  }

  private void doSend(InputStream inputStream, OutputStream outputStream) {
    String requestTarget = new HttpRequest(inputStream).getRequestTarget();

    MessageResponser messageResponser = abstractMessageResponserFactory.createMessageResponser(requestTarget);
    messageResponser.doSend(outputStream);
  }

}
