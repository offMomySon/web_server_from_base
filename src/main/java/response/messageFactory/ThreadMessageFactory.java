package response.messageFactory;

import domain.RequestTarget;
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
  public Message createMessage(RequestTarget requestTarget) {
    return new SimpleMessage(NOT_AVAILABLE_THREAD);
  }

  @Override
  public boolean isSupported(RequestTarget requestTarget) {
    return !statusSnapShot.isAvailable();
  }
}
