package response.messageFactory;

import domain.ResourcePath;
import lombok.NonNull;
import response.message.content.SimpleMessage;
import response.message.sender.Message;
import thread.snapshot.ThreadStatusSnapShot;

public class ThreadNotExistMessageFactory implements AbstractMessageFactory {
    private final static String NOT_AVAILABLE_THREAD = "not available thread.";

    private final ThreadStatusSnapShot statusSnapShot;

    public ThreadNotExistMessageFactory(@NonNull ThreadStatusSnapShot statusSnapShot) {
        this.statusSnapShot = statusSnapShot;
    }

    @Override
    public Message createMessage(ResourcePath resourcePath) {
        return new SimpleMessage(NOT_AVAILABLE_THREAD);
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return !statusSnapShot.isAvailable();
    }
}
