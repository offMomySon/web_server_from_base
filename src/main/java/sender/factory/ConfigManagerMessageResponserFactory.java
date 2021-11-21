package sender.factory;

import config.ConfigManager;
import sender.strategy.MessageResponser;
import sender.strategy.WelcomPageMessageResponser;

public class ConfigManagerMessageResponserFactory implements AbstractMessageResponserFactory{
  private final ConfigManager configManager;

  public ConfigManagerMessageResponserFactory(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public MessageResponser createMessageResponser(String path) {
    return new WelcomPageMessageResponser(configManager);
  }

  @Override
  public boolean isSupported(String path) {
    return configManager.isWelcomePageUri(path);
  }
}
