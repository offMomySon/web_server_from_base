package response.messageFactory;

import domain.ResourcePath;
import response.message.sender.Message;

import java.util.List;

public class CompositeMessageFactory implements AbstractMessageFactory {
    private final List<AbstractMessageFactory> factories;

    public CompositeMessageFactory(List<AbstractMessageFactory> factories) {
        this.factories = factories;
    }

    @Override
    public Message createMessage(ResourcePath resourcePath) {
        return factories.stream()
                .filter(factory -> factory.isSupported(resourcePath))
                .findFirst()
                .map(factory -> factory.createMessage(resourcePath))
                .orElseThrow(() -> new RuntimeException("resourcePath = " + resourcePath));
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return factories.stream().anyMatch(factory -> factory.isSupported(resourcePath));
    }
}
