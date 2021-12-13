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
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return factories.stream()
                .filter(factory -> factory.isSupported(hostAddress, resourcePath))
                .findFirst()
                .map(factory -> factory.createMessage(hostAddress, resourcePath))
                .orElseThrow();
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return factories.stream().anyMatch(factory -> factory.isSupported(hostAddress, resourcePath));
    }
}
