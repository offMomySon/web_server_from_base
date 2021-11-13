import static resource.ResourceStatus.PROCESSABLE_THREAD_NOT_EXIST;

import config.ConfigManager;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sender.MessageManager;

public class ThreadController {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ExecutorService executorService;
  private final ConfigManager configManager;
  private final MessageManager messageManager;

  private final int usableThreadCount;
  private final int waitableThreadCount;
  private int currentUsingThreadCount = 0;
  private final int currentWaitThreadCount = 0;

  public ThreadController(ConfigManager configManager) {
    this.executorService = ForkJoinPool.commonPool();

    this.configManager = configManager;
    this.usableThreadCount = configManager.getUsableThreadCount();
    this.waitableThreadCount = configManager.getWaitableThreadCount();

    this.messageManager = new MessageManager(configManager);
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

  public synchronized void decreaseUsableThread() {
    currentUsingThreadCount -= 1;
  }

  private synchronized void increaseWaitableThread() {
    currentUsingThreadCount += 1;
  }

  private synchronized void decreaseWaitableThread() {
    currentUsingThreadCount -= 1;
  }

  public void waitUntilProcessable() {
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

  public void process(Socket socket) {
    try {
      if (isProcessable()) {
        executorService.submit(
            new Servlet(socket.getInputStream(), socket.getOutputStream(), configManager,
                messageManager, this));
      } else {
        messageManager.sendMessage("", PROCESSABLE_THREAD_NOT_EXIST, socket.getOutputStream());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
