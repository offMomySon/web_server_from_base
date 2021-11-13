package sender.factory;

import config.ConfigManager;
import java.io.OutputStream;
import resource.ResourceStatus;
import sender.strategy.DirectoryMessageResponser;
import sender.strategy.FileMessageResponser;
import sender.strategy.MessageResponser;
import sender.strategy.NotFoundMessageResponser;
import sender.strategy.WelcomPageMessageResponser;

public class MessageResponserFactory {

  public static MessageResponser getMessageResponser(OutputStream outputStream,
      ConfigManager configManager, ResourceStatus resourceStatus, String filePath) {
    MessageResponser messageResponser = null;

    switch (resourceStatus) {
      case FILE_EXIST:
        messageResponser = new FileMessageResponser(outputStream, filePath);
        break;
      case DIRECTORY_EXIST:
        messageResponser = new DirectoryMessageResponser(outputStream, filePath);
        break;
      case PATH_NOT_EXIST:
        messageResponser = new NotFoundMessageResponser(outputStream);
        break;
      case WELCOMPAGE:
        messageResponser = new WelcomPageMessageResponser(outputStream, configManager);
    }

    return messageResponser;
  }
}
