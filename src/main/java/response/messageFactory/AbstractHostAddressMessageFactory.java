package response.messageFactory;

import domain.ResourcePath;
import response.message.sender.Message;

public abstract class AbstractHostAddressMessageFactory implements AbstractMessageFactory {
    private final String hostAddress;

    public AbstractHostAddressMessageFactory(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public abstract Message createMessage(String hostAddress, ResourcePath resourcePath);

    public abstract boolean isSupported(String hostAddress, ResourcePath resourcePath);

    @Override
    public Message createMessage(ResourcePath resourcePath) {
        return createMessage(hostAddress, resourcePath);
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return isSupported(hostAddress, resourcePath);
    }
}