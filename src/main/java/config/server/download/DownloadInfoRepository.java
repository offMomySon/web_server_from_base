package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.download.data.DownloadInfoAtIp;
import config.server.download.data.DownloadRate;
import domain.FileExtension;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class DownloadInfoRepository {
    private final Map<String, DownloadInfoAtIp> values;
    private final DownloadInfoAtIp commonIp;

    private DownloadInfoRepository(@NonNull Map<String, DownloadInfoAtIp> values,
                                   @NonNull DownloadInfoAtIp commonIp) {
        this.values = values;
        this.commonIp = commonIp;
    }

    public static DownloadInfoRepository create(DownloadRate downloadRate,
                                                Set<FileExtension> restrictedFileExtension,
                                                Set<DownloadInfoAtIp> downloadInfoAtIps) {

        Map<String, DownloadInfoAtIp> newValues = downloadInfoAtIps.stream()
                .collect(Collectors.toMap(DownloadInfoAtIp::getHostAddress, Function.identity(), (it1, it2) -> it1));

        return new DownloadInfoRepository(newValues, DownloadInfoAtIp.allMatched(downloadRate, restrictedFileExtension));
    }

    public DownloadInfoAtIp find(@NonNull String ip) {
        return values.getOrDefault(ip, commonIp);
    }
}
