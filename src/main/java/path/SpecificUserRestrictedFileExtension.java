package path;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import lombok.extern.slf4j.Slf4j;
import response.sender.RequestSender;
import response.sender.RestrictedFileExtensionRequestSender;
import response.sender.ThreadRelatedRequestSender;

@Slf4j
public class SpecificUserRestrictedFileExtension implements AbstractRequestTargetChecker {
  private final String clientIpAddress;

  public SpecificUserRestrictedFileExtension(String clientIpAddress) {
    this.clientIpAddress = clientIpAddress;
  }

  @Override
  public RequestSender messageSend(String requestPath) {
    DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
    String extension = requestPath.substring(requestPath.lastIndexOf("."));

    log.info("SpecificUserRestrictedFileExtension containsRestrictedFileExtensionAtIp = {} ", downloadConfig.containsRestrictedFileExtensionAtIp(clientIpAddress, extension));
    if (downloadConfig.containsRestrictedFileExtensionAtIp(clientIpAddress, extension)) {
      return new RestrictedFileExtensionRequestSender();
    }
    return new ThreadRelatedRequestSender();
  }

  @Override
  public boolean isMatchedPath(String requestPath) {
    DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
    log.info("SpecificUserRestrictedFileExtension isMatchedPath = {} ", downloadConfig.containsIpAddress(clientIpAddress));
    return downloadConfig.containsIpAddress(clientIpAddress);
  }
}
