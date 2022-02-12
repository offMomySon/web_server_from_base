package config.server.download2;

import config.server.download2.data.DownloadInfoAtHostAddress;
import config.server.download2.data.DownloadRate2;
import domain.FileExtension;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DownloadInfoRepository {
    private final Map<String, DownloadInfoAtHostAddress> values;
    private final DownloadInfoAtHostAddress commonHostAddress;

    public DownloadInfoRepository(@NonNull Map<String, DownloadInfoAtHostAddress> values,
                                  @NonNull DownloadInfoAtHostAddress commonHostAddress) {
        this.values = values;
        this.commonHostAddress = commonHostAddress;
    }

    public static DownloadInfoRepository create(Set<DownloadInfoAtHostAddress> privateHostAddresses,
                                                DownloadRate2 downloadRate,
                                                Set<FileExtension> restrictedFileExtensions) {

        Map<String, DownloadInfoAtHostAddress> newValues = privateHostAddresses.stream()
                .collect(Collectors.toMap(DownloadInfoAtHostAddress::getIp, Function.identity(), (it1, it2) -> it1));

        return new DownloadInfoRepository(newValues, DownloadInfoAtHostAddress.allMatched(downloadRate, restrictedFileExtensions));
    }

    public DownloadInfoAtHostAddress find(String ip) {
        return values.getOrDefault(ip, commonHostAddress);
    }
}
