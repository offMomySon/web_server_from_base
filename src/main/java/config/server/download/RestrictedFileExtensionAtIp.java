package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RestrictedFileExtensionAtIp {
  private final String ip;
  private final List<String> restrictedFileExtension;

  @JsonCreator
  public RestrictedFileExtensionAtIp(@JsonProperty("ip") String ip, @JsonProperty("restrictedFileExtension") List<String> restrictedFileExtension) {
    this.ip = ip;
    this.restrictedFileExtension = restrictedFileExtension;

    log.info(ip);
    log.info(String.valueOf(restrictedFileExtension));
  }

  public boolean compareIpAddress(String clientIpAddress) {
    return ip.equals(clientIpAddress);
  }

  public boolean containsRestrictedFileExtension(String fileExtension) {
    return restrictedFileExtension.contains(fileExtension);
  }
}
