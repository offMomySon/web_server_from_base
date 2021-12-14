package response.messageFactory;

import config.ConfigManager;
import domain.ResourcePath;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

// TODO
public class RestrictedExtensionAtIpMessageFactory implements AbstractMessageFactory {
    private final String RESTRICTED_EXTENSION_AT_IP_MESSAGE = "제한된 파일 확장자 입니다.";
    private final FileMessageFactory fileMessageFactory = new FileMessageFactory();

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {

        if (ConfigManager.getInstance().getDownloadConfig().containsRestrictedFileExtensionAtIp(hostAddress, resourcePath.createFileExtension())) {
            return new SimpleMessage(RESTRICTED_EXTENSION_AT_IP_MESSAGE);
        }

        return fileMessageFactory.createMessage(hostAddress, resourcePath);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return ConfigManager.getInstance().getDownloadConfig().containsIpAddress(hostAddress);
    }
}
