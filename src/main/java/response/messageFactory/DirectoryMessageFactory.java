package response.messageFactory;

import domain.ResourcePath;
import response.message.content.DirectoryMessage;
import response.message.sender.Message;

public class DirectoryMessageFactory implements AbstractMessageFactory {
    @Override
    public Message createMessage(ResourcePath resourcePath) {
        return new DirectoryMessage(resourcePath.toString());
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return resourcePath.isDirectory();
    }
}
