package time;

import config.ConfigManager;
import config.server.download.DownloadInfoRestrictChecker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DownloadInfoTest {

    @DisplayName("다운로드 가능하지 않으면 false 를 출력합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"192.168.0.44"})
    void isPossibleDownload_fail_case(String ip) {
        //given
        DownloadInfoRestrictChecker downloadInfoRestrictChecker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();

        for (int i = 0; i < 10; i++) {
            if (!downloadInfoRestrictChecker.isRestrictedCount(ip)) {
//                downloadInfoRestrictChecker.increaseCount(ip);
            }
        }

        //when
        boolean actual = downloadInfoRestrictChecker.isRestrictedCount(ip);

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @DisplayName("다운로드 가능하면 true 를 출력합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"192.168.0.44"})
    void isPossibleDownload_success_case(String ip) {
        //given
        DownloadInfoRestrictChecker downloadInfoRestrictChecker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();

        //when
        boolean actual = downloadInfoRestrictChecker.isRestrictedCount(ip);

        //then
        Assertions.assertThat(actual).isTrue();
    }
}