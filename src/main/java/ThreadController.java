import config.server.thread.ThreadConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.HttpRequest;
import sender.factory.AbstractMessageResponserFactory;
import sender.factory.thread.ThreadStatus;
import sender.strategy.MessageResponser;

public class ThreadController implements ThreadStatus {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ExecutorService executorService;

  private final int usableThreadCount;
  private final int waitableThreadCount;
  private int currentUsingThreadCount = 0;
  private int currentWaitThreadCount = 0;

  public ThreadController(ThreadConfig threadConfig) {
    this.executorService = ForkJoinPool.commonPool();

    this.usableThreadCount = threadConfig.getUsableThreadCount();
    this.waitableThreadCount = threadConfig.getWaitableThreadCount();
  }

  private boolean isProcessable() {
    return existUsableThread() || existWaitableThread();
  }

  private boolean existUsableThread() {
    return currentUsingThreadCount < usableThreadCount;
  }

  private boolean existWaitableThread() {
    return currentWaitThreadCount < waitableThreadCount;
  }

  private synchronized void increaseUsableThread() {
    currentUsingThreadCount += 1;
  }

  private synchronized void decreaseUsableThread() {
    currentUsingThreadCount -= 1;
  }

  private synchronized void increaseWaitableThread() {
    currentUsingThreadCount += 1;
  }

  private synchronized void decreaseWaitableThread() {
    currentUsingThreadCount -= 1;
  }

  private void waitUntilProcessable() {
    if (!existUsableThread()) {
      increaseWaitableThread();

      while (true) {
        if (existUsableThread()) {
          break;
        }
      }

      decreaseWaitableThread();
    }
    increaseUsableThread();
  }

  public void process(Socket socket, AbstractMessageResponserFactory factory) {
    if(!isProcessable()){
      return;
    }

    try {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        executorService.submit(() -> {
          waitUntilProcessable();

          doSend(inputStream, outputStream, factory);

          decreaseUsableThread();
        });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static void doSend(InputStream inputStream, OutputStream outputStream, AbstractMessageResponserFactory factory){
    String requestTarget = new HttpRequest(inputStream).getRequestTarget();

    MessageResponser messageResponser = factory.createMessageResponser(requestTarget);
    messageResponser.doSend(outputStream);
  }

  @Override
  public boolean isAvailable() {
    return isProcessable();
  }
}
