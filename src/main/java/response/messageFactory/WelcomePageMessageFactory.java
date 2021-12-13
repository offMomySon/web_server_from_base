package response.messageFactory;

import config.ConfigManager;
import domain.ResourcePath;
import response.message.content.WelcomePageMessage;
import response.message.sender.Message;

public class WelcomePageMessageFactory implements AbstractMessageFactory {
    private final ConfigManager configManager;

    public WelcomePageMessageFactory(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return new WelcomePageMessage(configManager);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return configManager.isWelcomePage(resourcePath);
    }
}
