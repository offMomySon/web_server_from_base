package response.messageFactory.composite;

import java.util.List;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;

public class CompositeMessageFactory implements AbstractMessageFactory {
  private final List<AbstractMessageFactory> factories;

  public CompositeMessageFactory(List<AbstractMessageFactory> factories) {
    this.factories = factories;
  }

  @Override
  public Message createMessage(String requestTarget) {
    return factories.stream()
        .filter(factory -> factory.isSupported(requestTarget))
        .findFirst()
        .map(factory -> factory.createMessage(requestTarget))
        .orElseThrow();
  }

  @Override
  public boolean isSupported(String path) {
    return factories.stream().anyMatch(factory -> factory.isSupported(path));
  }
}
