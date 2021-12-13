package response.messageFactory;

import config.ConfigManager;
import domain.ResourcePath;
import lombok.extern.slf4j.Slf4j;
import response.message.content.DirectoryMessage;
import response.message.content.FileMessage;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

@Slf4j
public class PathMessageFactory implements AbstractMessageFactory {
    private final static String FILE_NOT_FOUND = "File not exist";

    private final ConfigManager configManager = ConfigManager.getInstance();

    @Override
    public Message createMessage(ResourcePath resourcePath) {
        ResourcePath downloadTarget = ConfigManager.getInstance().getDownloadConfig().getResourcePath(resourcePath);

        log.info("FilePath = {}", resourcePath);

        if (resourcePath.exists() && resourcePath.isFile()) {
            return new FileMessage(resourcePath.createFileExtension(), resourcePath.createDownloadFile());
        }

        if (resourcePath.exists() && resourcePath.isDirectory()) {
            return new DirectoryMessage(resourcePath.toString());
        }

        return new SimpleMessage(FILE_NOT_FOUND);
    }

    @Override
    public boolean isSupported(ResourcePath resourcePath) {
        return true;
    }
}
