package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadWorker {
  private final int maxValue;
  private AtomicInteger currentValue;

  public ThreadWorker(int maxValue) {
    this.maxValue = maxValue;
  }

  public void runWithOccupied(Runnable runnable) {
    currentValue.incrementAndGet();
    runnable.run();
    currentValue.decrementAndGet();
  }

  public boolean isFull() {
    return maxValue <= currentValue.get();
  }

}
