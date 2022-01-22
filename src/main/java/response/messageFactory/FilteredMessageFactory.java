package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadInfoRestrictChecker;
import domain.FileExtension;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

@Slf4j
public class FilteredMessageFactory extends AbstractHostAddressMessageFactory {
    private final String RESTRICTED_EXTENSION_MESSAGE = "Restricted File extension.";
    private final FileExtension fileExtension;

    public FilteredMessageFactory(@NonNull String hostAddress,
                                  @NonNull FileExtension fileExtension) {
        super(hostAddress);
        this.fileExtension = fileExtension;
    }

    @Override
    public Message createMessage(String hostAddress) {
        return new SimpleMessage(RESTRICTED_EXTENSION_MESSAGE);
    }

    @Override
    public boolean isSupported(String hostAddress) {
        DownloadInfoRestrictChecker downloadInfoRestrictChecker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();

        return downloadInfoRestrictChecker.isRestrictedFileExtension(hostAddress, fileExtension);
    }
}
