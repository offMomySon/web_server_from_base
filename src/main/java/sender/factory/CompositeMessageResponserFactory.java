package sender.factory;

import java.util.List;
import sender.strategy.MessageResponser;

public class CompositeMessageResponserFactory implements AbstractMessageResponserFactory {
  private final List<AbstractMessageResponserFactory> factories;

  public CompositeMessageResponserFactory(List<AbstractMessageResponserFactory> factories) {
    this.factories = factories;
  }

  @Override
  public MessageResponser createMessageResponser(String requestTarget) {
    return factories.stream()
        .filter(factory -> factory.isSupported(requestTarget))
        .findFirst()
        .map(factory -> factory.createMessageResponser(requestTarget))
        .orElseThrow();
  }

  @Override
  public boolean isSupported(String path) {
    return factories.stream().anyMatch(factory -> factory.isSupported(path));
  }
}
