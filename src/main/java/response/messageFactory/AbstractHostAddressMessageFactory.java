package response.messageFactory;

import domain.ResourcePath;
import lombok.NonNull;
import response.message.sender.Message;

public abstract class AbstractHostAddressMessageFactory implements AbstractMessageFactory {
    private final String hostAddress;

    public AbstractHostAddressMessageFactory(@NonNull String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public abstract Message createMessage(@NonNull String hostAddress, @NonNull ResourcePath resourcePath);

    public abstract boolean isSupported(@NonNull String hostAddress, @NonNull ResourcePath resourcePath);

    @Override
    public Message createMessage(ResourcePath resourcePath) {
        return createMessage(hostAddress, resourcePath);
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return isSupported(hostAddress, resourcePath);
    }
}