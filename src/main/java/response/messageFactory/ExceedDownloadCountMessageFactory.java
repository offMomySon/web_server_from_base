package response.messageFactory;

import config.server.download.DownloadInfo;
import config.server.download.DownloadInfoRepository;
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
        DownloadInfo downloadInfo = DownloadInfoRepository.getDownloadInfo(hostAddress);

        return new ExceedDownloadMessage(downloadInfo);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        log.info("called ExceedDownloadCountMessageFactory isSupported");
        if (!DownloadInfoRepository.contains(hostAddress)) {
            return false;
        }

        DownloadInfo downloadInfo = DownloadInfoRepository.getDownloadInfo(hostAddress);

        log.info("downloadInfoAtHostAddress = {}", downloadInfo);

        return !downloadInfo.isPossibleDownload();
    }
}
