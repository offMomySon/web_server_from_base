package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DownloadRate {
    private final int count;
    private final long period;

    @JsonCreator
    public DownloadRate(@JsonProperty("count") int count, @JsonProperty("period") long period) {
        this.count = count;
        this.period = period;
    }
}
