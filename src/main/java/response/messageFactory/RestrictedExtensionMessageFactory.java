package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import domain.ResourcePath;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

public class RestrictedExtensionMessageFactory implements AbstractMessageFactory {
    private final String RESTRICTED_EXTENSION_MESSAGE = "제한된 파일 확장자 입니다.";

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return new SimpleMessage(RESTRICTED_EXTENSION_MESSAGE);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();

        return downloadConfig.containsRestrictedFileExtension(resourcePath.createFileExtension());
    }
}
