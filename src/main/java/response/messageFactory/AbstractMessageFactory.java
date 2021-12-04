package response.messageFactory;

import response.message.sender.Message;

public interface AbstractMessageFactory {

  Message createMessage(String requestTarget);

  boolean isSupported(String filePath);
}
