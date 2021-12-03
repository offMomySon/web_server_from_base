package sender.factory;

import config.ConfigManager;
import java.util.List;
import thread.ThreadStatus;

public class OrderedMessageResponserFactories {
  private final List<AbstractMessageResponserFactory> factories;

  public OrderedMessageResponserFactories(ThreadStatus threadStatus, ConfigManager configManager) {
    this.factories = List.of(new ThreadMessageResponserFactory(threadStatus), new WelcomePageMessageResponserFactory(configManager), new PathMessageResponserFactory(configManager));
  }

  public AbstractMessageResponserFactory create() {
    return new CompositeMessageResponserFactory(this.factories);
  }
}
