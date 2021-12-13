package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.FileExtension;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Getter
@Slf4j
public class RestrictedFileExtensionAtIp {
    private final String ip;
    private final Set<FileExtension> restrictedFileExtension;

    @JsonCreator
    public RestrictedFileExtensionAtIp(@JsonProperty("ip") String ip, @JsonProperty("restrictedFileExtension") Set<FileExtension> restrictedFileExtension) {
        this.ip = ip;
        this.restrictedFileExtension = restrictedFileExtension;

        log.info(ip);
        log.info("RestrictedFileExtension = {}", restrictedFileExtension);
    }

    public boolean compareIpAddress(String clientIpAddress) {
        return ip.equals(clientIpAddress);
    }

    public boolean containsRestrictedFileExtension(FileExtension fileExtension) {
        return restrictedFileExtension.contains(fileExtension);
    }
}
