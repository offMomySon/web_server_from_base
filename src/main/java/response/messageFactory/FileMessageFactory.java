package response.messageFactory;

import config.server.download.DownloadInfo;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import response.message.content.FileMessage;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

import java.io.File;
import java.time.Instant;

@Slf4j
public class FileMessageFactory implements AbstractMessageFactory {
    private final String FILE_NOT_EXIST_MESSAGE = "Not exist file.";

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        File downloadFile = resourcePath.createDownloadFile();

        if (resourcePath.isFile()) {
            DownloadInfo downloadInfo = DownloadInfo.getDownloadInfoAtHostAddress(hostAddress);
            log.info("downloadInfo = {}", downloadInfo);

            downloadInfo.addRequestTime(Instant.now());

            return new FileMessage(resourcePath.createFileExtension(), downloadFile);
        }
        return new SimpleMessage(FILE_NOT_EXIST_MESSAGE);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return resourcePath.isFile();
    }
}
