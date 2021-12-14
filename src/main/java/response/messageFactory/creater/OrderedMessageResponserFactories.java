package response.messageFactory.creater;

import response.messageFactory.*;
import thread.snapshot.ThreadStatusSnapShot;

import java.util.List;

public class OrderedMessageResponserFactories {
    private final List<AbstractMessageFactory> factories;

    public OrderedMessageResponserFactories(ThreadStatusSnapShot statusSnapShot) {
        this.factories = List.of(
                new ThreadMessageFactory(statusSnapShot),
                new WelcomePageMessageFactory(),
                new DirectoryMessageFactory(),
                new CompositedFileMessageFactoryWithFilter(new OrderedFileMessageFactoriesWithFilter().create()),
                new FileMessageFactory());
    }

    public AbstractMessageFactory create() {
        return new CompositeMessageFactory(this.factories);
    }
}
