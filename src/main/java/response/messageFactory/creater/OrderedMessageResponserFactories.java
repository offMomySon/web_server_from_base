package response.messageFactory.creater;

import config.ConfigManager;
import java.util.List;
import response.messageFactory.AbstractMessageFactory;
import response.messageFactory.PathMessageFactory;
import response.messageFactory.ThreadMessageFactory;
import response.messageFactory.WelcomePageMessageFactory;
import response.messageFactory.composite.CompositeMessageFactory;
import thread.snapshot.ThreadStatusSnapShot;

public class OrderedMessageResponserFactories {
  private final List<AbstractMessageFactory> factories;

  public OrderedMessageResponserFactories(ThreadStatusSnapShot statusSnapShot, ConfigManager configManager) {
    this.factories = List.of(new ThreadMessageFactory(statusSnapShot), new WelcomePageMessageFactory(configManager), new PathMessageFactory());
  }

  public AbstractMessageFactory create() {
    return new CompositeMessageFactory(this.factories);
  }
}
