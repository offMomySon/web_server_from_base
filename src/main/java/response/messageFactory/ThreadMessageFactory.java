package response.messageFactory;

import domain.ResourcePath;
import response.message.content.SimpleMessage;
import response.message.sender.Message;
import thread.snapshot.ThreadStatusSnapShot;

public class ThreadMessageFactory implements AbstractMessageFactory {
    private final static String NOT_AVAILABLE_THREAD = "not available thread.";

    private ThreadStatusSnapShot statusSnapShot;

    public ThreadMessageFactory(ThreadStatusSnapShot statusSnapShot) {
        this.statusSnapShot = statusSnapShot;
    }

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return new SimpleMessage(NOT_AVAILABLE_THREAD);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return !statusSnapShot.isAvailable();
    }
}
