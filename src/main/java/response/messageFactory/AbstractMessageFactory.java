package response.messageFactory;

import domain.ResourcePath;
import response.message.sender.Message;

public interface AbstractMessageFactory {

    Message createMessage(ResourcePath resourcePath);

    boolean isSupported(ResourcePath resourcePath);
}
