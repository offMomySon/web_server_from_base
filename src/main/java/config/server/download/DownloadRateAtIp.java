package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DownloadRateAtIp {
    private final String hostAddress;
    private final DownloadRate downloadRate;

    @JsonCreator
    public DownloadRateAtIp(@JsonProperty("ip") String hostAddress, @JsonProperty("downloadRate") DownloadRate downloadRate) {
        this.hostAddress = hostAddress;
        this.downloadRate = downloadRate;
    }
}
