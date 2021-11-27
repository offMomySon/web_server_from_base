package sender.factory;

import config.ConfigManager;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import sender.strategy.DirectoryMessageResponser;
import sender.strategy.FileMessageResponser;
import sender.strategy.MessageResponser;
import sender.strategy.SimpleMessageResponser;

@Slf4j
public class PathMessageResponserFactory implements AbstractMessageResponserFactory {
  private final static String FILE_NOT_FOUND = "file not exist";

  private final ConfigManager configManager;

  public PathMessageResponserFactory(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  public MessageResponser createMessageResponser(String targetPath) {
    String filePath = configManager.creatFilePath(targetPath);
    File file = new File(filePath);

    if (file.exists() && file.isFile()) {
      return new FileMessageResponser(filePath);
    }

    if (file.exists() && file.isDirectory()) {
      return new DirectoryMessageResponser(filePath);
    }

    return new SimpleMessageResponser(FILE_NOT_FOUND);
  }

  @Override
  public boolean isSupported(String filePath) {
    return true;
  }
}
