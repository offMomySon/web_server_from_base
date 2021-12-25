package time;

import config.ConfigManager;
import download.DownloadInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static download.DownloadInfo.createDownloadInfo;

class DownloadInfoTest {

    @DisplayName("다운로드 가능하지 않으면 false 를 출력합니다.")
    @Test
    void isPossibleDownload_fail_case() {
        //given
        DownloadInfo downloadInfo = createDownloadInfo("192.168.0.44");
        downloadInfo.recordRequestTime(Instant.now());
        downloadInfo.recordRequestTime(Instant.now());
        downloadInfo.recordRequestTime(Instant.now());

        //when
        boolean actual = downloadInfo.isPossibleDownload();

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @DisplayName("다운로드 가능하면 true 를 출력합니다.")
    @Test
    void isPossibleDownload_success_case() {
        //given
        DownloadInfo downloadInfo = createDownloadInfo("192.168.0.44");
        downloadInfo.recordRequestTime(Instant.now());
        downloadInfo.recordRequestTime(Instant.now());
        downloadInfo.recordRequestTime(Instant.now());

        long period = ConfigManager.getInstance().getDownloadConfig().getDownloadRate().getPeriod();

        try {
            Thread.sleep(period + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //when
        boolean actual = downloadInfo.isPossibleDownload();

        //then
        Assertions.assertThat(actual).isTrue();
    }
}