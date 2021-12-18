package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DownloadRateAtHostAddress {
    private final String hostAddress;
    private final DownloadRate downloadRate;

    @JsonCreator
    public DownloadRateAtHostAddress(@JsonProperty("ip") String hostAddress, @JsonProperty("downloadRate") DownloadRate downloadRate) {
        this.hostAddress = hostAddress;
        this.downloadRate = downloadRate;
    }

    public boolean isEqualAtHostAddress(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        return hostAddress.equals(hostAddress);
    }
}
