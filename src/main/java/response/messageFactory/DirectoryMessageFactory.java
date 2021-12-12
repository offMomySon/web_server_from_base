package response.messageFactory;

import domain.RequestTarget;
import response.message.content.DirectoryMessage;
import response.message.sender.Message;

public class DirectoryMessageFactory implements AbstractMessageFactory{
    @Override
    public Message createMessage(RequestTarget requestTarget) {
        return new DirectoryMessage(requestTarget.toString());
    }

    @Override
    public boolean isSupported(RequestTarget requestTarget) {
        return requestTarget.isDirectory();
    }
}
