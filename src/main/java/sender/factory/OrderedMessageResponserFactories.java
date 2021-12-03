package sender.factory;

import config.ConfigManager;
import java.util.List;

public class OrderedMessageResponserFactories {
  private final List<AbstractMessageResponserFactory> factories;

  public OrderedMessageResponserFactories(ConfigManager configManager) {
    this.factories = List.of(new WelcomePageMessageResponserFactory(configManager), new PathMessageResponserFactory(configManager));
  }

  public AbstractMessageResponserFactory create() {
    return new CompositeMessageResponserFactory(this.factories);
  }
}
