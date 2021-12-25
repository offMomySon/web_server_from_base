package response.messageFactory;

import domain.ResourcePath;
import download.DownloadInfo;
import download.DownloadInfoRepository;
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
        DownloadInfo downloadInfo = DownloadInfoRepository.getInstance().createIfAbsent(hostAddress);

        return new ExceedDownloadMessage(downloadInfo);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        log.info("called ExceedDownloadCountMessageFactory isSupported");
        DownloadInfo downloadInfo = DownloadInfoRepository.getInstance().createIfAbsent(hostAddress);

        log.info("downloadInfoAtHostAddress = {}", downloadInfo);

        return !downloadInfo.isPossibleDownload();
    }
}
