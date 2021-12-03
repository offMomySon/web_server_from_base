import config.server.thread.ThreadConfig;
import lombok.extern.slf4j.Slf4j;
import sender.factory.thread.ThreadStatusSnapshot;
import thread.ThreadWorker;

@Slf4j
public class ThreadControllerSnapshot {
  private final ThreadWorker currentUsingThreadCount;
  private final ThreadWorker currentWaitThreadCount;

  public ThreadControllerSnapshot(ThreadConfig threadConfig) {
    currentUsingThreadCount = new ThreadWorker(threadConfig.getUsableThreadCount());
    currentWaitThreadCount = new ThreadWorker(threadConfig.getWaitableThreadCount());
  }

  public boolean isProcessable() {
    return existUsableThread() || existWaitableThread();
  }

  public boolean existUsableThread() {
    return !currentUsingThreadCount.isFull();
  }

  private boolean existWaitableThread() {
    return !currentWaitThreadCount.isFull();
  }

  public void waitIfNotExistUseableThread() {
    currentWaitThreadCount.runWithOccupied(() -> {
      while (!existUsableThread()) {
      }
    });
  }

  public void runWithOccupiedWorkerThread(Runnable runnable) {
    currentUsingThreadCount.runWithOccupied(runnable);
  }

  public ThreadStatusSnapshot createSnapShot() {
    boolean isProcessable = isProcessable();
    return () -> isProcessable;
  }
}
