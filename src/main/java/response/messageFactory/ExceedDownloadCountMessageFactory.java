package response.messageFactory;

import config.server.download.DownloadInfo;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import response.message.content.ExceedDownloadMessage;
import response.message.sender.Message;

@Slf4j
public class ExceedDownloadCountMessageFactory implements AbstractMessageFactory {
    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        DownloadInfo downloadInfo = DownloadInfo.getDownloadInfoAtHostAddress(hostAddress);

        return new ExceedDownloadMessage(downloadInfo);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        log.info("called ExceedDownloadCountMessageFactory isSupported");
        DownloadInfo downloadInfo = DownloadInfo.getDownloadInfoAtHostAddress(hostAddress);

        log.info("downloadInfoAtHostAddress = {}", downloadInfo);

        return !downloadInfo.isPossibleDownload();
    }
}
