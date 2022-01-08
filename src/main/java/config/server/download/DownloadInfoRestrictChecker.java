package config.server.download;

import config.server.download.data.DownloadInfoAtIp;
import domain.FileExtension;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

// TODO 3
// 파사드 패턴?
public class DownloadInfoRestrictChecker {
    private final Map<String, DownloadInfoRestrict> values = new HashMap<>();
    private final DownloadInfoRepository downloadInfoRepository;

    public DownloadInfoRestrictChecker(@NonNull DownloadInfoRepository downloadInfoRepository) {
        this.downloadInfoRepository = downloadInfoRepository;
    }

    private DownloadInfoRestrict find(String ip) {
        return values.computeIfAbsent(ip, it -> DownloadInfoRestrict.from(downloadInfoRepository.find(it)));
    }

    public boolean isRestrictedFileExtension(@NonNull String ip, @NonNull FileExtension fileExtension) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isRestrictedFileExtension(fileExtension);
    }

    // TODO 2
    // DownloadInfoRestrictChecker 는 rate 제한인데..
    // DownloadInfoRestrict 는 왜 count 를 써야하지..
    public boolean isRestrictedRate(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isRestrictedCount();
    }

    public void decreaseCount(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        values.put(ip, downloadInfoRestrict.decreaseCount());
    }

    public void increaseCount(@NonNull String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        values.put(ip, downloadInfoRestrict.increaseCount());
    }
}
