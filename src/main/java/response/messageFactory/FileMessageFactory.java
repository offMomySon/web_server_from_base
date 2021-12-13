package response.messageFactory;

import domain.ResourcePath;
import response.message.content.FileMessage;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

import java.io.File;

public class FileMessageFactory implements AbstractMessageFactory {
    private final String FILE_NOT_EXIST_MESSAGE = "존재하지 않는 파일 입니다.";

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        File downloadFile = resourcePath.createDownloadFile();

        if (resourcePath.isFile()) {
            return new FileMessage(resourcePath.createFileExtension(), downloadFile);
        }
        return new SimpleMessage(FILE_NOT_EXIST_MESSAGE);
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return resourcePath.isFile();
    }
}
