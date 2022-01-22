package response.messageFactory;

import response.message.sender.Message;

public interface AbstractMessageFactory {

    Message createMessage();

    boolean isSupported();
}
