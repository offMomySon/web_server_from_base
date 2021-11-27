package sender.factory;

import config.ConfigManager;
import sender.strategy.MessageResponser;
import sender.strategy.WelcomPageMessageResponser;

public class WelcomePageMessageResponserFactory implements AbstractMessageResponserFactory {
  private final ConfigManager configManager;

  public WelcomePageMessageResponserFactory(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public MessageResponser createMessageResponser(String filePath) {
    return new WelcomPageMessageResponser(configManager);
  }

  @Override
  public boolean isSupported(String filePath) {
    return configManager.isWelcomePage(filePath);
  }
}
