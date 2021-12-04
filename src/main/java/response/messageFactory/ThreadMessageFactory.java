package response.messageFactory;

import response.message.sender.Message;
import response.message.content.SimpleMessage;
import thread.snapshot.ThreadStatusSnapShot;

public class ThreadMessageFactory implements AbstractMessageFactory {
  private final static String NOT_AVAILABLE_THREAD = "not available thread.";

  private ThreadStatusSnapShot statusSnapShot;

  public ThreadMessageFactory(ThreadStatusSnapShot statusSnapShot) {
    this.statusSnapShot = statusSnapShot;
  }

  @Override
  public Message createMessage(String filePath) {
    return new SimpleMessage(NOT_AVAILABLE_THREAD);
  }

  @Override
  public boolean isSupported(String filePath) {
    return !statusSnapShot.isAvailable();
  }
}
