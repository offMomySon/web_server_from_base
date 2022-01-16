package action;

import domain.ResourcePath;
import lombok.NonNull;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;

import java.util.function.Function;

public class MainTaskActionCreator implements ActionCreator {
    private final AbstractMessageFactory targetMessageFactory;
    private final Function<Message, Runnable> messageHandler;

    public MainTaskActionCreator(@NonNull AbstractMessageFactory targetMessageFactory, @NonNull Function<Message, Runnable> messageHandler) {
        this.targetMessageFactory = targetMessageFactory;
        this.messageHandler = messageHandler;
    }

    @Override
    public Runnable get(@NonNull ResourcePath resourcePath) {
        Message message = targetMessageFactory.createMessage(resourcePath);

        return messageHandler.apply(message);
    }
}
