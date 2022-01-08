package config.server.download.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class DownloadRate {
    private static final int MIN_COUNT = 1;
    private static final int MIN_SECOND = 1;

    private final Duration period;
    private final long count;

    private DownloadRate(long count, @NonNull Duration period) {
        this.count = validate(count);
        this.period = validate(period);
    }

    @JsonCreator
    public static DownloadRate create(@JsonProperty("count") long count,
                                      @JsonProperty("period") long period) {
        return new DownloadRate(count, Duration.ofMillis(period));
    }

    private long validate(long count) {
        if (MIN_COUNT > count) {
            throw new IllegalArgumentException("count 는 최소 1회 이상이어야 합니다.");
        }
        return count;
    }

    private Duration validate(Duration duration) {
        if (MIN_SECOND > duration.getSeconds()) {
            throw new IllegalArgumentException("period 는 최소 1초 이상이어야 합니다.");
        }
        return duration;
    }
}
