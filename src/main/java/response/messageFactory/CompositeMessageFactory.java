package response.messageFactory;

import lombok.NonNull;
import response.message.sender.Message;

import java.util.Collections;
import java.util.List;

public class CompositeMessageFactory implements AbstractMessageFactory {
    private final List<AbstractMessageFactory> factories;

    public CompositeMessageFactory(@NonNull List<AbstractMessageFactory> factories) {
        this.factories = Collections.unmodifiableList(factories);
    }

    @Override
    public Message createMessage() {
        return factories.stream()
                .filter(AbstractMessageFactory::isSupported)
                .findFirst()
                .map(AbstractMessageFactory::createMessage)
                .orElseThrow();
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
