package sender.factory;

import config.ConfigManager;
import java.util.List;
import thread.ThreadStatusSnapShot;

public class OrderedMessageResponserFactories {
  private final List<AbstractMessageResponserFactory> factories;

  public OrderedMessageResponserFactories(ThreadStatusSnapShot statusSnapShot, ConfigManager configManager) {
    this.factories = List.of(new ThreadMessageResponserFactory(statusSnapShot), new WelcomePageMessageResponserFactory(configManager), new PathMessageResponserFactory(configManager));
  }

  public AbstractMessageResponserFactory create() {
    return new CompositeMessageResponserFactory(this.factories);
  }
}
