package response.messageFactory;

import domain.RequestTarget;
import response.message.sender.Message;

public interface AbstractMessageFactory {

  Message createMessage(RequestTarget requestTarget);

  boolean isSupported(RequestTarget requestTarget);
}
