package config.server.download;

import domain.FileExtension;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DownloadInfoRestrictChecker {
    private final Map<String, DownloadInfoRestrict> values = new HashMap<>();
    private final DownloadInfoRepository downloadInfoRepository;

    public DownloadInfoRestrictChecker(@NonNull DownloadInfoRepository downloadInfoRepository) {
        this.downloadInfoRepository = downloadInfoRepository;
    }

    private DownloadInfoRestrict find(String ip) {
        return values.computeIfAbsent(ip, it -> DownloadInfoRestrict.from(downloadInfoRepository.find(it)));
    }

    public boolean isRestricted(@NonNull String ip, @NonNull FileExtension fileExtension) {
        return isRestrictedCount(ip) && isRestrictedFileExtension(ip, fileExtension);
    }

    public boolean isRestrictedFileExtension(@NonNull String ip, @NonNull FileExtension fileExtension) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isRestrictedFileExtension(fileExtension);
    }

    public boolean isRestrictedCount(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isRestrictedCount();
    }

    public void useResource(@NonNull String ip) {
        if (isRestrictedCount(ip)) {
            return;
        }

        decreaseCount(ip);

        CompletableFuture.runAsync(() -> {
            increaseCount(ip);
        }, CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
    }

    private void decreaseCount(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        values.put(ip, downloadInfoRestrict.decreaseCount());
    }

    private void increaseCount(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        values.put(ip, downloadInfoRestrict.increaseCount());
    }
}
