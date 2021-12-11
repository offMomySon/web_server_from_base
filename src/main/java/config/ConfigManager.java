package config;

import config.server.BasicConfig;
import config.server.download.DownloadConfig;
import config.server.thread.ThreadConfig;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.startLine.RequestTarget;

@Getter
@Slf4j
@Builder(access = AccessLevel.PRIVATE)
public class ConfigManager {
  private static ConfigManager configManager = getInstance();

  private final BasicConfig basicConfig;
  private final ThreadConfig threadConfig;
  private final DownloadConfig downloadConfig;

  public static ConfigManager getInstance() {
    if (configManager != null) {
      return configManager;
    }
    return configManager = ConfigManager.create();
  }

  private static ConfigManager create() {
    return ConfigManager.builder()
        .basicConfig(BasicConfig.create())
        .threadConfig(ThreadConfig.create())
        .downloadConfig(DownloadConfig.create())
        .build();
  }

  public boolean isWelcomePage(RequestTarget requestTarget) {
    return basicConfig.isWelcomePage(requestTarget);
  }

  public RequestTarget getDownloadPath() {
    return RequestTarget.create(downloadConfig.getDownloadPath());
  }
}

