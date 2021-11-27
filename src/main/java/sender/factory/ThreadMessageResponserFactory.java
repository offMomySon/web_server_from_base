package sender.factory;

import sender.factory.thread.ThreadStatus;
import sender.strategy.MessageResponser;
import sender.strategy.SimpleMessageResponser;

public class ThreadMessageResponserFactory implements AbstractMessageResponserFactory {
  private final static String NOT_AVAILABLE_THREAD = "not available thread.";

  private final ThreadStatus threadStatus;

  public ThreadMessageResponserFactory(ThreadStatus threadStatus) {
    this.threadStatus = threadStatus;
  }

  @Override
  public MessageResponser createMessageResponser(String filePath) {
    return new SimpleMessageResponser(NOT_AVAILABLE_THREAD);
  }

  @Override
  public boolean isSupported(String filePath) {
    return !threadStatus.isAvailable();
  }
}
