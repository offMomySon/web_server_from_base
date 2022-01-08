package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadInfoRestrictChecker;
import config.server.download.data.DownloadConfig;
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
        DownloadInfoRestrictChecker downloadInfoRestrictChecker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();
        FileExtension fileExtension = resourcePath.createFileExtension();

        if (downloadInfoRestrictChecker.isRestrictedFileExtension(hostAddress, fileExtension)) {
            return true;
        }
        return false;
    }
}
