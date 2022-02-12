package config.server.download2;

import config.server.download2.data.DownloadInfoAtHostAddress;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DownloadInfoAtHostAddresses {
    private final Map<String, DownloadInfoAtHostAddress> values;

    public DownloadInfoAtHostAddresses(@NonNull Map<String, DownloadInfoAtHostAddress> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    public static DownloadInfoAtHostAddresses from(Set<DownloadInfoAtHostAddress> downloadInfoAtHostAddresses) {
        Map<String, DownloadInfoAtHostAddress> newValues = downloadInfoAtHostAddresses.stream()
                .collect(Collectors.toMap(DownloadInfoAtHostAddress::getIp, Function.identity(), (it1, it2) -> it1));

        return new DownloadInfoAtHostAddresses(newValues);
    }

    public DownloadInfoAtHostAddress find(String ip, DownloadInfoAtHostAddress downloadInfoAtHostAddress) {
        return values.getOrDefault(ip, downloadInfoAtHostAddress);
    }
}
