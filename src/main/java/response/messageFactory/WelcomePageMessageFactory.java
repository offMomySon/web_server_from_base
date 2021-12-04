package response.messageFactory;

import config.ConfigManager;
import response.message.sender.Message;
import response.message.content.WelcomePageMessage;

public class WelcomePageMessageFactory implements AbstractMessageFactory {
  private final ConfigManager configManager;

  public WelcomePageMessageFactory(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public Message createMessage(String filePath) {
    return new WelcomePageMessage(configManager);
  }

  @Override
  public boolean isSupported(String filePath) {
    return configManager.isWelcomePage(filePath);
  }
}
