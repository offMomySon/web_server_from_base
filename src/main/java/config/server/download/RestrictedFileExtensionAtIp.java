package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import domain.FileExtension;

@Getter
@Slf4j
public class RestrictedFileExtensionAtIp {
  private final String ip;
  private final List<FileExtension> restrictedFileExtension;

  @JsonCreator
   public RestrictedFileExtensionAtIp(@JsonProperty("ip") String ip, @JsonProperty("restrictedFileExtension") List<FileExtension> restrictedFileExtension) {
    this.ip = ip;
    this.restrictedFileExtension = restrictedFileExtension;

    log.info(ip);
    log.info("RestrictedFileExtension = {}" ,String.valueOf(restrictedFileExtension));
  }

  public boolean compareIpAddress(String clientIpAddress) {
    return ip.equals(clientIpAddress);
  }

  public boolean containsRestrictedFileExtension(String fileExtension) {
    return restrictedFileExtension.contains(fileExtension);
  }
}
