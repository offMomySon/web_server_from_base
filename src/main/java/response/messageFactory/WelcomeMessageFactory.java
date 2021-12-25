package response.messageFactory;

import config.ConfigManager;
import domain.ResourcePath;
import response.message.content.WelcomePageMessage;
import response.message.sender.Message;

public class WelcomeMessageFactory implements AbstractMessageFactory {

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return new WelcomePageMessage(ConfigManager.getInstance());
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return ConfigManager.getInstance().isWelcomePage(resourcePath);
    }
}
