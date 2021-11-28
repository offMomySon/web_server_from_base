package config;

import config.server.BasicConfig;
import config.server.download.DownloadConfig;
import config.server.thread.ThreadConfig;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder(access = AccessLevel.PRIVATE)
public class ConfigManager {
  private final BasicConfig basicConfig;
  private final ThreadConfig threadConfig;
  private final DownloadConfig downloadConfig;

  public static ConfigManager create() {
    return ConfigManager.builder()
        .basicConfig(BasicConfig.create())
        .threadConfig(ThreadConfig.create())
        .downloadConfig(DownloadConfig.create())
        .build();
  }

  public boolean isWelcomePage(String filePath) {
    return basicConfig.isWelcomePage(filePath);
  }

  public String creatFilePath(String target) {
    return downloadConfig.getDownloadPath() + target;
  }
}

