package response.messageFactory.creater;

import response.messageFactory.*;
import thread.snapshot.ThreadStatusSnapShot;

import java.util.List;

public class OrderedMessageResponserFactories {
    private final List<AbstractMessageFactory> factories;

    public OrderedMessageResponserFactories(String hostAddress, ThreadStatusSnapShot statusSnapShot) {
        this.factories = List.of(
                new ThreadNotExistMessageFactory(statusSnapShot),
                new WelcomeMessageFactory(),
                new DirectoryMessageFactory(),
                new FilteredMessageFactory(hostAddress),
                new FileMessageFactory());
    }

    public AbstractMessageFactory create() {
        return new CompositeMessageFactory(this.factories);
    }
}
