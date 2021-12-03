package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.Config;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DownloadConfig {
  private static final String DOWNLOAD_CONFIG_PATH = "src/main/resources/config/download_setting.json";

  private final String downloadPath;
  private final int period;
  private final int count;
  private final List<String> restrictedFileExtension;
  private final List<RestrictedFileExtensionAtIp> restrictedFileExtensionAtIps;
  private final List<PeriodCountConfigAtIp> periodCountConfigAtIps;

  @JsonCreator
  public DownloadConfig(@JsonProperty("downloadPath") String downloadPath, @JsonProperty("period") int period, @JsonProperty("count") int count,
      @JsonProperty("restrictedFileExtension") List<String> restrictedFileExtension,
      @JsonProperty("restrictedFileExtensionAtIps") List<RestrictedFileExtensionAtIp> restrictedFileExtensionAtIps,
      @JsonProperty("periodCountConfigAtIps") List<PeriodCountConfigAtIp> periodCountConfigAtIps) {
    this.downloadPath = downloadPath;
    this.period = period;
    this.count = count;
    this.restrictedFileExtension = restrictedFileExtension;
    this.restrictedFileExtensionAtIps = restrictedFileExtensionAtIps;
    this.periodCountConfigAtIps = periodCountConfigAtIps;

  }

  public static DownloadConfig create() {
    DownloadConfig downloadConfig = new Config<>(DownloadConfig.DOWNLOAD_CONFIG_PATH, DownloadConfig.class).create();
    return downloadConfig;
  }

}
