package config.server.download;

import lombok.Getter;

@Getter
public class PeriodCountConfigAtIp {

  private String ip;
  private int period;
  private int count;
}
