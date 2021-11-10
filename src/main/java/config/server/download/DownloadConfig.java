package config.server.download;

import java.util.List;
import lombok.Getter;

@Getter
public class DownloadConfig {

  private String downloadPath;
  private int period;
  private int count;
  private List<String> restrictedFileExtension;
  private List<RestrictedFileExtensionAtIp> restrictedFileExtensionAtIps;
  private List<PeriodCountConfigAtIp> periodCountConfigAtIps;

}
