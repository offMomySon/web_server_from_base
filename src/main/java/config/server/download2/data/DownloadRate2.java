package config.server.download2.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class DownloadRate2 {
    private static final int ONE_SECOND = 1;
    private static final int ONE_COUNT = 1;

    private final Duration period;
    private final long count;

    private DownloadRate2(Duration period, long count) {
        this.period = validate(period);
        this.count = validate(count);
    }

    @JsonCreator
    public static DownloadRate2 create(@JsonProperty("period") long period,
                                       @JsonProperty("count") long count) {
        Duration duration = Duration.ofMillis(period);

        return new DownloadRate2(duration, count);
    }

    private static Duration validate(Duration period) {
        if (ONE_SECOND > period.getSeconds()) {
            throw new IllegalArgumentException("period 는 1 초 이상이어야 합니다.");
        }
        return period;
    }

    private static Long validate(Long count) {
        if (ONE_COUNT > count) {
            throw new IllegalArgumentException("count 는 1 회 이상이어야 합니다.");
        }
        return count;
    }

    public long getCount() {
        return 0;
    }
}
