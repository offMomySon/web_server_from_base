package domain;

import config.ConfigManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class DownloadTargetTest {

    @Test
    @DisplayName("argument 의 시작부분에 download path 를 가지면 안됩니다.")
    void create() {
        //given
        String requestTarget = ConfigManager.getInstance().getDownloadConfig().getDownloadPath() + "/test1.txt";

        //when
        Throwable throwable = Assertions.catchThrowable(()->{
            DownloadTarget downloadTarget = DownloadTarget.create(Path.of(requestTarget));
        });

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}