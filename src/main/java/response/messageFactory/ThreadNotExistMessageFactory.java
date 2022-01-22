package response.messageFactory;

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
    public Message createMessage() {
        return new SimpleMessage(NOT_AVAILABLE_THREAD);
    }

    @Override
    public boolean isSupported() {
        return !statusSnapShot.isAvailable();
    }
}
