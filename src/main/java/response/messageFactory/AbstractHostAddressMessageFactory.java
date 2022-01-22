package response.messageFactory;

import lombok.NonNull;
import response.message.sender.Message;

public abstract class AbstractHostAddressMessageFactory implements AbstractMessageFactory {
    private final String hostAddress;

    public AbstractHostAddressMessageFactory(@NonNull String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public abstract Message createMessage(@NonNull String hostAddress);

    public abstract boolean isSupported(@NonNull String hostAddress);

    @Override
    public Message createMessage() {
        return createMessage(hostAddress);
    }

    @Override
    public boolean isSupported() {
        return isSupported(hostAddress);
    }
}