package sender.factory;

import sender.factory.thread.ThreadStatus;
import sender.strategy.MessageResponser;
import sender.strategy.NoThreadAvailableMessageResponser;

public class ThreadMessageResponserFactory implements AbstractMessageResponserFactory{

  private final ThreadStatus status;

  public ThreadMessageResponserFactory(ThreadStatus status) {
    this.status = status;
  }

  @Override
  public MessageResponser createMessageResponser(String path) {
    return new NoThreadAvailableMessageResponser();
  }

  @Override
  public boolean isSupported(String path) {
    return !status.isAvailable();
  }
}
