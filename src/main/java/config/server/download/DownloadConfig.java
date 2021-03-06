package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.Config;
import domain.FileExtension;
import domain.ResourcePath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
public class DownloadConfig {
    private static final String DOWNLOAD_CONFIG_PATH = "src/main/resources/config/download_setting.json";

    private final String downloadPath;
    private final DownloadRate downloadRate;
    private final Set<FileExtension> restrictedFileExtension;
    private final Set<RestrictedFileExtensionAtHostAddress> restrictedFileExtensionAtHostAddresses;
    private final List<DownloadRateAtHostAddress> downloadRateAtHostAddresses;

    @JsonCreator
    public DownloadConfig(@JsonProperty("downloadPath") String downloadPath,
                          @JsonProperty("downloadRate") DownloadRate downloadRate,
                          @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtension,
                          @JsonProperty("restrictedFileExtensionAtIps") Set<RestrictedFileExtensionAtHostAddress> restrictedFileExtensionAtHostAddresses,
                          @JsonProperty("downloadRateAtIps") List<DownloadRateAtHostAddress> downloadRateAtHostAddresses) {

        this.downloadPath = downloadPath;
        this.downloadRate = downloadRate;
        this.restrictedFileExtension = restrictedFileExtension;
        this.restrictedFileExtensionAtHostAddresses = restrictedFileExtensionAtHostAddresses;
        this.downloadRateAtHostAddresses = downloadRateAtHostAddresses;

        log.info("DownloadPath = {}", downloadPath);
        log.info("DownloadRate = {}", downloadRate);
        log.info("RestrictedFileExtension = {}", restrictedFileExtension);
        log.info("RestrictedFileExtensionAtIpsString = {}", restrictedFileExtensionAtHostAddresses);
        log.info("PeriodCountConfigAtIps = {}", downloadRateAtHostAddresses);
    }

    public static DownloadConfig create() {
        DownloadConfig downloadConfig = new Config<>(DownloadConfig.DOWNLOAD_CONFIG_PATH, DownloadConfig.class).create();
        return downloadConfig;
    }

    //with? at?
    // ????????? ip ??? download config ????????? ?????? ????????? hostAddress ??? ??????????????? ?????? ??????.
    public boolean containsDownloadRateInfoAtHostAddress(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new RuntimeException("????????? null ?????? ????????????.");
        }

        return downloadRateAtHostAddresses.stream().anyMatch(d -> d.isEqualAtHostAddress(hostAddress));
    }

    public DownloadRateAtHostAddress getDownloadRateInfoAtHostAddress(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new RuntimeException("????????? null ?????? ????????????.");
        }

        return downloadRateAtHostAddresses.stream()
                .filter(d -> d.isEqualAtHostAddress(hostAddress))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("hostAddress ??? ?????? downloadRateInfo ??? ???????????? ????????????."));
    }

    public ResourcePath getRootPath() {
        return ResourcePath.create(getDownloadPath());
    }

    public ResourcePath getResourcePath(ResourcePath resourcePath) {
        return getRootPath().append(resourcePath);
    }

    public boolean containsHostAddressAtRestrictedFileExtension(String hostAddress) {
        for (RestrictedFileExtensionAtHostAddress restrictedFileExtensionAtHostAddress : restrictedFileExtensionAtHostAddresses) {
            if (restrictedFileExtensionAtHostAddress.compareIpAddress(hostAddress)) {
                return true;
            }
        }
        return false;
    }

    //set contain overriding ?????? ??? ?????? ????????? ?????????.
    public boolean containsRestrictedFileExtensionAtHostAddress(String hostAddress, FileExtension fileExtension) {
        for (RestrictedFileExtensionAtHostAddress restrictedFileExtensionAtHostAddress : restrictedFileExtensionAtHostAddresses) {
            if (restrictedFileExtensionAtHostAddress.compareIpAddress(hostAddress) && restrictedFileExtensionAtHostAddress.containsRestrictedFileExtension(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRestrictedFileExtension(FileExtension fileExtension) {
        log.info("containsRestrictedFileExtension = {} ", restrictedFileExtension.contains(fileExtension));
        return restrictedFileExtension.contains(fileExtension);
    }
}
