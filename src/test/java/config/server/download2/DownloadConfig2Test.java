package config.server.download2;

import config.server.download2.data.DownloadConfig2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DownloadConfig2Test {

    @DisplayName("config file 로 부터 FileExtension 을 읽어 옵니다.")
    @Test
    void create() {
        //given
        //when
//        DownloadConfig2 downloadConfig = DownloadConfig2.ofJackson();
        DownloadConfig2 downloadConfig = DownloadConfig2.create();

        //thenR
    }
}