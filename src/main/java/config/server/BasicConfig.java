package config.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.ResourcePath;
import lombok.Getter;

@Getter
public class BasicConfig {
    private static final String BASIC_CONFIG_PATH = "src/main/resources/config/basic_setting.json";

    private final int port;
    private final ResourcePath welcomePageURI;

    @JsonCreator
    public BasicConfig(@JsonProperty("port") int port, @JsonProperty("welcomePagePath") ResourcePath welcomePageURI) {
        this.port = port;
        this.welcomePageURI = welcomePageURI;
    }

    public static BasicConfig create() {
        BasicConfig basicConfig = new Config<>(BasicConfig.BASIC_CONFIG_PATH, BasicConfig.class).create();
        return basicConfig;
    }

    public boolean isWelcomePage(ResourcePath resourcePath) {
        return welcomePageURI.equals(resourcePath);
    }
}
