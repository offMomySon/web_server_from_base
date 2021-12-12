package response.messageFactory;

import domain.ResourcePath;
import response.message.content.FileMessage;
import response.message.sender.Message;

import java.io.File;

public class FileMessageFactory implements AbstractMessageFactory {
    @Override
    public Message createMessage(ResourcePath resourcePath) {
        File downloadFile = resourcePath.createDownloadFile();

        return new FileMessage(downloadFile);
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return resourcePath.isFile();
    }
}
