package response.messageFactory;

import config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import domain.RequestTarget;
import response.message.content.DirectoryMessage;
import response.message.content.FileMessage;
import response.message.sender.Message;
import response.message.content.SimpleMessage;

@Slf4j
public class PathMessageFactory implements AbstractMessageFactory {
  private final static String FILE_NOT_FOUND = "File not exist";

  private final ConfigManager configManager = ConfigManager.getInstance();

  @Override
  public Message createMessage(RequestTarget requestTarget) {
    RequestTarget downloadTarget = ConfigManager.getInstance().getDownloadConfig().getResourcePath(requestTarget);

    log.info("FilePath = {}", requestTarget);

    if (requestTarget.exists() && requestTarget.isFile()) {
      return new FileMessage(requestTarget.toString());
    }

    if (requestTarget.exists() && requestTarget.isDirectory()) {
      return new DirectoryMessage(requestTarget.toString());
    }

    return new SimpleMessage(FILE_NOT_FOUND);
  }

  @Override
  public boolean isSupported(RequestTarget requestTarget) {
    return true;
  }
}
