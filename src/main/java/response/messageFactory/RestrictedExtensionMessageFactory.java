package response.messageFactory;

import reader.httpspec.startLine.RequestTarget;
import response.message.sender.Message;

public class RestrictedExtensionMessageFactory implements AbstractMessageFactory {

    @Override
    public Message createMessage(RequestTarget requestTarget) {

        return null;
    }

    @Override
    public boolean isSupported(RequestTarget requestTarget) {

        return false;
    }
}
