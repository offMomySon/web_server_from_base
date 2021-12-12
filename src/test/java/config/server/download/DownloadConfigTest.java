package config.server.download;

import domain.FileExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DownloadConfigTest {

    @Test
    void create() {
        DownloadConfig downloadConfig = DownloadConfig.create();
        Set<FileExtension> restrictedFileExtension = downloadConfig.getRestrictedFileExtension();

        for(FileExtension fileExtension : restrictedFileExtension){
            System.out.println(fileExtension.toString());
        }
    }

    @DisplayName("config file 로 부터 FileExtension 을 읽어 옵니다.")
    @Test
    void create2() {
        DownloadConfig downloadConfig = DownloadConfig.create();
        Set<RestrictedFileExtensionAtIp> restrictedFileExtensionAtIps = downloadConfig.getRestrictedFileExtensionAtIps();

        for(RestrictedFileExtensionAtIp restrictedFileExtensionAtIp :restrictedFileExtensionAtIps){
            System.out.println(restrictedFileExtensionAtIp.getRestrictedFileExtension());
        }
    }
}