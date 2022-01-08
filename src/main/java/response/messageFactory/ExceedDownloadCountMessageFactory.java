package response.messageFactory;

import config.ConfigManager;
import config.server.download.DownloadInfoRepository;
import config.server.download.DownloadInfoRestrictChecker;
import config.server.download.data.DownloadInfoAtIp;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import response.message.content.ExceedDownloadMessage;
import response.message.sender.Message;

@Slf4j
public class ExceedDownloadCountMessageFactory extends AbstractHostAddressMessageFactory {
    public ExceedDownloadCountMessageFactory(String hostAddress) {
        super(hostAddress);
    }

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        DownloadInfoRepository downloadInfoRepository = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRepository();
        DownloadInfoAtIp downloadInfoAtIp = downloadInfoRepository.find(hostAddress);

        return new ExceedDownloadMessage(downloadInfoAtIp);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        log.info("called ExceedDownloadCountMessageFactory isSupported");

        DownloadInfoRestrictChecker downloadInfoRestrictChecker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();

        if (downloadInfoRestrictChecker.isRestrictedRate(hostAddress)) {
            return true;
        }

        return false;
    }
}
