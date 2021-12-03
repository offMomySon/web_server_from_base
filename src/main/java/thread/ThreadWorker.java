package thread;

public class ThreadWorker {
  private final int usableThread;
  private int currentThread = 0;

  public ThreadWorker(int usableThread) {
    this.usableThread = usableThread;
  }

  public boolean isLeft() {
    return currentThread < usableThread;
  }

  public synchronized void increase() {
    currentThread++;
  }

  public synchronized void decrease() {
    currentThread--;
  }
}
