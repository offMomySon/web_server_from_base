package response.messageFactory.creater;

import response.messageFactory.*;
import thread.snapshot.ThreadStatusSnapShot;

import java.util.List;

public class OrderedMessageResponserFactories {
    private final List<AbstractMessageFactory> factories;

    public OrderedMessageResponserFactories(ThreadStatusSnapShot statusSnapShot, String hostAddress) {
        this.factories = List.of(
                new ThreadNotExistMessageFactory(statusSnapShot),
                new WelcomeMessageFactory(),
                new DirectoryMessageFactory(),
                new FilteredMessageFactory(hostAddress, fileExtension),
                new FileMessageFactory());
    }

    public AbstractMessageFactory create() {
        return new CompositeMessageFactory(this.factories);
    }
}
