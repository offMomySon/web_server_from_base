package response.messageFactory;

import domain.ResourcePath;
import response.message.sender.Message;

public interface AbstractMessageFactory {

    Message createMessage(String hostAddress, ResourcePath resourcePath);

    boolean isSupported(String hostAddress, ResourcePath resourcePath);
}
