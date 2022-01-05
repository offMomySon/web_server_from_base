package config.server.download2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.Config;
import domain.FileExtension;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class DownloadConfig2 {
    private static final String DOWNLOAD_CONFIG_PATH = "src/main/resources/config/download_setting2.json";

    private final String downloadPath;
    private final DownloadInfoRepository downloadInfoRepository;

    public DownloadConfig2(@NonNull String downloadPath, @NonNull DownloadInfoRepository downloadInfoRepository) {
        this.downloadPath = validateExistFolder(downloadPath);
        this.downloadInfoRepository = downloadInfoRepository;
    }

    private static String validateExistFolder(String downloadPath) {
        return downloadPath;
    }

    @JsonCreator
    public static DownloadConfig2 ofJackson(@JsonProperty("downloadPath") String downloadPath,
                                            @JsonProperty("downloadRate") DownloadRate2 downloadRate,
                                            @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtensions,
                                            @JsonProperty("downloadInfoAtIps") Set<DownloadInfoAtHostAddress> downloadInfoAtHostAddresses) {

        return new DownloadConfig2(downloadPath,
                DownloadInfoRepository.create(downloadInfoAtHostAddresses, downloadRate, restrictedFileExtensions));
    }

    public static DownloadConfig2 create() {
        DownloadConfig2 downloadConfig = new Config<>(DownloadConfig2.DOWNLOAD_CONFIG_PATH, DownloadConfig2.class).create();
        return downloadConfig;
    }
}
