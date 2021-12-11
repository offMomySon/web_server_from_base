package response.messageFactory;

import response.message.sender.Message;

public class RestrictedExtensionMessageFactory implements AbstractMessageFactory {

    @Override
    public Message createMessage(String requestTarget) {

        return null;
    }

    @Override
    public boolean isSupported(String filePath) {

        return false;
    }
}
