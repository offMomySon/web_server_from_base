package response.messageFactory;

import config.ConfigManager;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import response.message.content.DirectoryMessage;
import response.message.content.FileMessage;
import response.message.sender.Message;
import response.message.content.SimpleMessage;

@Slf4j
public class PathMessageFactory implements AbstractMessageFactory {
  private final static String FILE_NOT_FOUND = "file not exist";

  private final ConfigManager configManager = ConfigManager.getInstance();

  @Override
  public Message createMessage(String targetPath) {
    String filePath = configManager.creatFilePath(targetPath);
    File file = new File(filePath);

    log.info("FilePath = {}", filePath);

    if (file.exists() && file.isFile()) {
      return new FileMessage(filePath);
    }

    if (file.exists() && file.isDirectory()) {
      return new DirectoryMessage(filePath);
    }

    return new SimpleMessage(FILE_NOT_FOUND);
  }

  @Override
  public boolean isSupported(String filePath) {
    return true;
  }
}
