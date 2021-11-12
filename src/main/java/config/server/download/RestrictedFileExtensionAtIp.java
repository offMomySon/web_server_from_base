package config.server.download;

import java.util.List;
import lombok.Getter;

@Getter
public class RestrictedFileExtensionAtIp {

  private String ip;
  private List<String> restrictedFileExtension;
}
