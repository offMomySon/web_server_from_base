package config.server.download;

import config.ConfigManager;
import domain.FileExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DownloadConfigTest {

    @DisplayName("config file 로 부터 FileExtension 을 읽어 옵니다.")
    @Test
    void create() {
        //given
        //when
        Throwable actual = Assertions.catchThrowable(() -> {
            DownloadConfig downloadConfig = DownloadConfig.create();
        });

        //then
        Assertions.assertThat(actual).isNull();
    }


    @DisplayName("제한된 파일확장 자이면 true 를 반환해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {".jpg", ".class"})
    void containsRestrictedFileExtension(String restrictedFileExtension) {
        //given
        DownloadConfig downloadConfig = DownloadConfig.create();
        FileExtension fileExtension = FileExtension.parse(restrictedFileExtension);

        //when
        boolean actual = downloadConfig.containsRestrictedFileExtension(fileExtension);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("특정 IP 의 제한된 파일확장 자이면 true 를 반환해야 합니다.")
    @ParameterizedTest
    @CsvSource(value = {"192.168.0.1, .jpg", "192.168.0.2, .class"})
    void containsRestrictedFileExtensionAtIp(String hostAddress, String extension) {
        //given
        FileExtension fileExtension = FileExtension.parse(extension);

        //when
        boolean actual = ConfigManager.getInstance().getDownloadConfig().containsRestrictedFileExtensionAtHostAddress(hostAddress, fileExtension);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("특정 IP 의 제한된 파일확장 자이면 true 를 반환해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"192.168.0.1", "192.168.0.2"})
    void containsIpAddress(String hostAddress) {
        //given
        //when
        boolean actual = ConfigManager.getInstance().getDownloadConfig().containsHostAddressAtRestrictedFileExtension(hostAddress);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("특정 hostAddress 가 download config 들 중에 포함되어있으면 true 를 반환해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"192.168.0.1", "192.168.0.2"})
    void containsHostAddressAtSpecificDownloadConfig(String hostAddress) {
        //given
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();

        //when
        boolean actual = downloadConfig.containsHostAddressAtSpecificDownloadConfig(hostAddress);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("특정 hostAddress 가 download config 들 중에 포함되지 않는다면 false 를 반환해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"192.168.0.3", "192.168.0.4"})
    void not_containsHostAddressAtSpecificDownloadConfig(String hostAddress) {
        //given
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();

        //when
        boolean actual = downloadConfig.containsHostAddressAtSpecificDownloadConfig(hostAddress);

        //then
        Assertions.assertThat(actual).isFalse();
    }
}