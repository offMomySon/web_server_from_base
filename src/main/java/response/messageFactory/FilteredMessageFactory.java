package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import domain.FileExtension;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

@Slf4j
public class FilteredMessageFactory extends AbstractHostAddressMessageFactory {
    private final String RESTRICTED_EXTENSION_MESSAGE = "Restricted File extension.";

    public FilteredMessageFactory(String hostAddress) {
        super(hostAddress);
    }

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return new SimpleMessage(RESTRICTED_EXTENSION_MESSAGE);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
        FileExtension fileExtension = resourcePath.createFileExtension();

        if (downloadConfig.containsHostAddressAtRestrictedFileExtension(hostAddress)) {
            if (downloadConfig.containsRestrictedFileExtensionAtHostAddress(hostAddress, fileExtension)) {
                return true;
            }
            return false;
        } else {
            if (downloadConfig.containsRestrictedFileExtension(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
