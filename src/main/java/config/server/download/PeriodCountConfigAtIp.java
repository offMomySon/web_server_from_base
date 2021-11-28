package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PeriodCountConfigAtIp {
  private final String ip;
  private final int period;
  private final int count;

  @JsonCreator
  public PeriodCountConfigAtIp(@JsonProperty("ip") String ip, @JsonProperty("period") int period, @JsonProperty("count") int count) {
    this.ip = ip;
    this.period = period;
    this.count = count;
  }
}
