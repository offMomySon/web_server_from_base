package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import domain.RequestTarget;
import response.message.sender.Message;

public class RestrictedExtensionMessageFactory implements AbstractMessageFactory {

    @Override
    public Message createMessage(RequestTarget requestTarget) {

        return null;
    }

    @Override
    public boolean isSupported(RequestTarget requestTarget) {
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();


        return downloadConfig.containsRestrictedFileExtension(requestTarget.createFileExtension());
    }
}
