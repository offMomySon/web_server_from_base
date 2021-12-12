package response.messageFactory;

import java.util.List;

import domain.RequestTarget;
import response.message.sender.Message;

public class CompositeMessageFactory implements AbstractMessageFactory {
  private final List<AbstractMessageFactory> factories;

  public CompositeMessageFactory(List<AbstractMessageFactory> factories) {
    this.factories = factories;
  }

  @Override
  public Message createMessage(RequestTarget requestTarget) {
    return factories.stream()
        .filter(factory -> factory.isSupported(requestTarget))
        .findFirst()
        .map(factory -> factory.createMessage(requestTarget))
        .orElseThrow();
  }

  @Override
  public boolean isSupported(RequestTarget requestTarget) {
    return factories.stream().anyMatch(factory -> factory.isSupported(requestTarget));
  }
}
