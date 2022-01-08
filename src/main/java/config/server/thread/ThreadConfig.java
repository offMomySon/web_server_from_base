package config.server.thread;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.Config;
import lombok.Getter;

@Getter
public class ThreadConfig {
    private static final String THREAD_CONFIG_PATH = "src/main/resources/config/thread_setting.json";

    private final int usableThreadCount;
    private final int waitableThreadCount;

    @JsonCreator
    public ThreadConfig(@JsonProperty("usableThreadCount") int usableThreadCount, @JsonProperty("waitableThreadCount") int waitableThreadCount) {
        this.usableThreadCount = usableThreadCount;
        this.waitableThreadCount = waitableThreadCount;
    }

    public static ThreadConfig create() {
        ThreadConfig threadConfig = new Config<>(ThreadConfig.THREAD_CONFIG_PATH, ThreadConfig.class).create();
        return threadConfig;
    }
}
