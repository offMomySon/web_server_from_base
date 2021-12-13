package response.messageFactory.creater;

import config.ConfigManager;
import response.messageFactory.*;
import thread.snapshot.ThreadStatusSnapShot;

import java.util.List;

public class OrderedMessageResponserFactories {
    private final List<AbstractMessageFactory> factories;

    public OrderedMessageResponserFactories(ThreadStatusSnapShot statusSnapShot, ConfigManager configManager) {
        this.factories = List.of(
                new ThreadMessageFactory(statusSnapShot),
                new WelcomePageMessageFactory(configManager),
                new DirectoryMessageFactory(),
                new RestrictedExtensionAtIpMessageFactory(),
                new RestrictedExtensionMessageFactory(),
                new FileMessageFactory());
    }

    public AbstractMessageFactory create() {
        return new CompositeMessageFactory(this.factories);
    }
}
