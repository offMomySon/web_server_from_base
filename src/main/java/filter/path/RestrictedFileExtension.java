package filter.path;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import java.util.List;

public class RestrictedFileExtension implements AbstractPathChecker {
  // instance 변수 설정할 필요 있나
  private final List<String> restrictedFileExtension = ConfigManager.getInstance().getDownloadConfig().getRestrictedFileExtension();

  @Override
  public void messageSend(String requestPath, String clientIpAddress) {

  }

  @Override
  public boolean isMatchedPath(String requestPath, String clientIpAddress) {
    DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
    String extension = requestPath.substring(requestPath.lastIndexOf("."));

    return downloadConfig.containsRestrictedFileExtension(extension);
  }
}
