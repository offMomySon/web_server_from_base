package sender.factory;

import sender.strategy.MessageResponser;
import sender.strategy.SimpleMessageResponser;
import thread.ThreadStatusSnapShot;

public class ThreadMessageResponserFactory implements AbstractMessageResponserFactory {
  private final static String NOT_AVAILABLE_THREAD = "not available thread.";

  private ThreadStatusSnapShot statusSnapShot;

  public ThreadMessageResponserFactory(ThreadStatusSnapShot statusSnapShot) {
    this.statusSnapShot = statusSnapShot;
  }

  @Override
  public MessageResponser createMessageResponser(String filePath) {
    return new SimpleMessageResponser(NOT_AVAILABLE_THREAD);
  }

  @Override
  public boolean isSupported(String filePath) {
    return statusSnapShot.isAvailable();
  }
}
