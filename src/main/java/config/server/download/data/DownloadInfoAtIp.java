package config.server.download.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.FileExtension;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

@Getter
@Slf4j
public class DownloadInfoAtIp {
    private static final String ALL_MATCHED = "*";

    private final String hostAddress;
    private final DownloadRate downloadRate;
    private final Set<FileExtension> restrictedFileExtensions;

    @JsonCreator
    public DownloadInfoAtIp(@NonNull @JsonProperty("ip") String ip,
                            @JsonProperty("downloadRate") DownloadRate downloadRate,
                            @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtensions) {
        this.hostAddress = ip;
        this.downloadRate = downloadRate;
        this.restrictedFileExtensions = restrictedFileExtensions;

        log.info("Ip = {}", ip);
        log.info("downloadRate = {}", downloadRate);
        log.info("restrictedFileExtensions = {}", restrictedFileExtensions);
    }

    public static DownloadInfoAtIp allMatched(DownloadRate downloadRate, Set<FileExtension> restrictedFileExtensions) {
        return new DownloadInfoAtIp(ALL_MATCHED, downloadRate, restrictedFileExtensions);
    }
}
