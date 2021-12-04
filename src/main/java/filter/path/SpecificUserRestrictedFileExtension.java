package filter.path;

import config.ConfigManager;
import config.server.download.DownloadConfig;

public class SpecificUserRestrictedFileExtension implements AbstractPathChecker {
  private final String clientIpAddress;

  public SpecificUserRestrictedFileExtension(String clientIpAddress) {
    this.clientIpAddress = clientIpAddress;
  }

  @Override
  public void messageSend(String requestPath, String clientIpAddress) {

  }

  @Override
  public boolean isMatchedPath(String requestPath, String clientIpAddress) {
    DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();

    return downloadConfig.containsIpAddress(clientIpAddress);
  }
}
