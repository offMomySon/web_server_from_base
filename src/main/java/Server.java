import config.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import reader.HttpRequest;
import sender.factory.AbstractMessageResponserFactory;
import sender.factory.OrderedMessageResponserFactories;
import sender.strategy.MessageResponser;
import thread.ThreadCountManager;

@Slf4j
public class Server {
  private static final Socket UNBOUNDED = null;
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private final AbstractMessageResponserFactory responserFactory;
  private final ServerSocket serverSocket;
  private final ThreadCountManager threadCountManager;
  private final ConfigManager configManager;

  public Server(ConfigManager configManager) {
    this.configManager = configManager;
    serverSocket = createServerSocket(configManager.getBasicConfig().getPort());
    threadCountManager = createThreadController(configManager);
    responserFactory = new OrderedMessageResponserFactories(threadCountManager, configManager).create();
  }

  private static ServerSocket createServerSocket(int port) {
    try {
      return new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static ThreadCountManager createThreadController(ConfigManager configManager) {
    return new ThreadCountManager(configManager.getThreadConfig());
  }

  public void start() {
    Socket socket = UNBOUNDED;
    try {
      while (true) {
        log.info("waiting.. request");
        socket = serverSocket.accept();

        log.info("accept.. request");
        log.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress(), socket.getPort());

        doProcess(socket.getInputStream(), socket.getOutputStream());

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

  // 안티패턴?
  // 받을거면 InputStream 만 받는게 좋은가?,
//  public void process(Socket socket) {
//    try (InputStream inputStream = socket.getInputStream();
//        OutputStream outputStream = socket.getOutputStream();) {
//      doProcess(inputStream, outputStream);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  public void doProcess(InputStream inputStream, OutputStream outputStream) {
    if (!threadCountManager.isProcessable()) {
      return;
    }

    // 이 구문 전체가 동기화 보장되어야 할 거 같은데?..
    threadPool.execute(new Runnable() {
      @Override
      public synchronized void run() {
        threadCountManager.waitIfNotExistLeftThread();

        threadCountManager.runWithOccupiedWorkerThread(() -> doSend(inputStream, outputStream));
      }
    });
  }

  private void doSend(InputStream inputStream, OutputStream outputStream) {
    String requestTarget = new HttpRequest(inputStream).getRequestTarget();

    MessageResponser messageResponser = responserFactory.createMessageResponser(requestTarget);
    messageResponser.doSend(outputStream);
  }
}
