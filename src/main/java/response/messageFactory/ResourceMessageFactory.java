package response.messageFactory;

import domain.ResourceMessageCreator;
import lombok.NonNull;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

public class ResourceMessageFactory implements AbstractMessageFactory {
    private static final Message EMPTY_MESSAGE = new SimpleMessage("empty message");

    private final Message message;

    private ResourceMessageFactory(@NonNull Message message) {
        this.message = message;
    }

    public static ResourceMessageFactory create(@NonNull ResourceMessageCreator resourceMessageCreator) {
        Message message = resourceMessageCreator.createMessage().orElse(EMPTY_MESSAGE);

        return new ResourceMessageFactory(message);
    }


    @Override
    public Message createMessage() {
        if (isSupported()) {
            return message;
        }

        throw new RuntimeException("불가.");
    }

    @Override
    public boolean isSupported() {
        return message != EMPTY_MESSAGE;
    }
}
