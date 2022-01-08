package config.server.download.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.Config;
import config.server.download.DownloadInfoRepository;
import config.server.download.DownloadInfoRestrictChecker;
import domain.FileExtension;
import domain.ResourcePath;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Slf4j
@Getter
public class DownloadConfig {
    private static final String DOWNLOAD_CONFIG_PATH = "src/main/resources/config/download_setting.json";

    private final DownloadInfoRestrictChecker downloadInfoRestrictChecker;
    private final DownloadInfoRepository downloadInfoRepository;
    private final String downloadPath;

    private DownloadConfig(@NonNull String downloadPath,
                           @NonNull DownloadInfoRepository downloadInfoRepository,
                           @NonNull DownloadInfoRestrictChecker downloadInfoRestrictChecker) {
        this.downloadPath = validateExistFolder(downloadPath);
        this.downloadInfoRepository = downloadInfoRepository;
        this.downloadInfoRestrictChecker = downloadInfoRestrictChecker;

        log.info("DownloadPath = {}", downloadPath);
        log.info("downloadInfoRestrictChecker = {}", downloadInfoRestrictChecker);
    }

    @JsonCreator
    public static DownloadConfig ofJackson(@NonNull @JsonProperty("downloadPath") String downloadPath,
                                           @JsonProperty("downloadRate") DownloadRate downloadRate,
                                           @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtension,
                                           @JsonProperty("downloadInfoAtIps") Set<DownloadInfoAtIp> downloadInfoAtIps) {

        DownloadInfoRepository downloadInfoRepository = DownloadInfoRepository.create(downloadRate, restrictedFileExtension, downloadInfoAtIps);

        return new DownloadConfig(downloadPath, downloadInfoRepository, new DownloadInfoRestrictChecker(downloadInfoRepository));
    }

    public static DownloadConfig create() {
        DownloadConfig downloadConfig = new Config<>(DownloadConfig.DOWNLOAD_CONFIG_PATH, DownloadConfig.class).create();
        return downloadConfig;
    }

    private String validateExistFolder(String downloadPath) {
        if (Files.notExists(Path.of(downloadPath))) {
            throw new IllegalArgumentException("download path 가 존재하지 않습니다.");
        }
        return downloadPath;
    }

    public ResourcePath getRootPath() {
        return ResourcePath.create(getDownloadPath());
    }

    public ResourcePath getResourcePath(ResourcePath resourcePath) {
        return getRootPath().append(resourcePath);
    }

}
