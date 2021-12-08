package path;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import response.sender.RequestSender;
import response.sender.RestrictedFileExtensionRequestSender;

@Slf4j
public class RestrictedFileExtension implements RequestTargetChecker {
  // instance 변수 설정할 필요 있나
  private final List<String> restrictedFileExtension = ConfigManager.getInstance().getDownloadConfig().getRestrictedFileExtension();

  @Override
  public RequestSender messageSend(String requestPath) {
    return new RestrictedFileExtensionRequestSender();
  }

  @Override
  public boolean isMatchedPath(String requestPath) {
    DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
    String extension = requestPath.substring(requestPath.lastIndexOf("."));
    log.info("downloadConfig.containsRestrictedFileExtension(extension) = {} ", downloadConfig.containsRestrictedFileExtension(extension));

    return downloadConfig.containsRestrictedFileExtension(extension);
  }
}
