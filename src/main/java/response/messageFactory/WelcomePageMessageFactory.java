package response.messageFactory;

import config.ConfigManager;
import domain.RequestTarget;
import response.message.sender.Message;
import response.message.content.WelcomePageMessage;

public class WelcomePageMessageFactory implements AbstractMessageFactory {
  private final ConfigManager configManager;

  public WelcomePageMessageFactory(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public Message createMessage(RequestTarget requestTarget) {
    return new WelcomePageMessage(configManager);
  }

  @Override
  public boolean isSupported(RequestTarget requestTarget) {
    return configManager.isWelcomePage(requestTarget);
  }
}
