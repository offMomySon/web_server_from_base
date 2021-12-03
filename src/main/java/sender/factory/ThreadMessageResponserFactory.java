package sender.factory;

import sender.factory.thread.ThreadStatusSnapshot;
import sender.strategy.MessageResponser;
import sender.strategy.SimpleMessageResponser;

public class ThreadMessageResponserFactory implements AbstractMessageResponserFactory {
  private final static String NOT_AVAILABLE_THREAD = "not available thread.";

  private final ThreadStatusSnapshot threadStatusSnapshot;

  public ThreadMessageResponserFactory(ThreadStatusSnapshot threadStatusSnapshot) {
    this.threadStatusSnapshot = threadStatusSnapshot;
  }

  public MessageResponser createMessageResponser() {
    return new SimpleMessageResponser(NOT_AVAILABLE_THREAD);
  }

  @Override
  public MessageResponser createMessageResponser(String filePath) {
    return createMessageResponser();
  }

  @Override
  public boolean isSupported(String filePath) {
    return !threadStatusSnapshot.isAvailable();
  }
}
