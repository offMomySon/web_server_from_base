package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import domain.ResourcePath;
import response.message.sender.Message;

public class RestrictedExtensionMessageFactory implements AbstractMessageFactory {

    @Override
    public Message createMessage(ResourcePath resourcePath) {

        return null;
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();


        return downloadConfig.containsRestrictedFileExtension(resourcePath.createFileExtension());
    }
}
