package config.server.download2.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.FileExtension;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class DownloadInfoAtHostAddress {
    private static final String ALL_MATCHED_IP = "*";
    private final String ip;
    private final Set<FileExtension> restrictedFileExtensions;
    private final DownloadRate2 downloadRate;

    @JsonCreator
    public DownloadInfoAtHostAddress(@NonNull @JsonProperty("ip") String ip,
                                     @JsonProperty("downloadRate") DownloadRate2 downloadRate,
                                     @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtensions) {
        this.ip = ip;
        this.downloadRate = downloadRate;
        this.restrictedFileExtensions = restrictedFileExtensions;

        log.info("Ip = {}", ip);
        log.info("downloadRate = {}", downloadRate);
        log.info("restrictedFileExtensions = {}", restrictedFileExtensions);
    }

    public static DownloadInfoAtHostAddress allMatched(DownloadRate2 downloadRate,
                                                       Set<FileExtension> fileExtensions) {
        return new DownloadInfoAtHostAddress(ALL_MATCHED_IP, downloadRate, fileExtensions);
    }

    public String getIp() {
        return ip;
    }

    public DownloadRate2 getDownloadRate() {
        return downloadRate;
    }

    public Set<FileExtension> getFileExtensions() {
        return restrictedFileExtensions;
    }
}
