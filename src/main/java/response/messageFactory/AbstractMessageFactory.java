package response.messageFactory;

import reader.httpspec.startLine.RequestTarget;
import response.message.sender.Message;

public interface AbstractMessageFactory {

  Message createMessage(RequestTarget requestTarget);

  boolean isSupported(RequestTarget requestTarget);
}
