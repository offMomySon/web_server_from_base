package config.server.download2;

import domain.FileExtension;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class DownloadInfoRestrictChecker {
    private final Map<String, DownloadInfoRestrict> values = new HashMap<>();
    private final DownloadInfoRepository downloadInfoRepository;

    public DownloadInfoRestrictChecker(@NonNull DownloadInfoRepository downloadInfoRepository) {
        this.downloadInfoRepository = downloadInfoRepository;
    }

    // TODO - comoputeIfAbsente
    private DownloadInfoRestrict find(String ip) {
        if (values.containsKey(ip)) {
            return values.get(ip);
        }

        DownloadInfoRestrict downloadInfoRestrict = DownloadInfoRestrict
                .from(downloadInfoRepository.find(ip));
        values.put(ip, downloadInfoRestrict);

        return downloadInfoRestrict;
    }

    public void decreaseCount(String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);

        values.put(ip, downloadInfoRestrict.decreaseCount());
    }

    public boolean isRestrictedRate(String ip) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isResctrictedCount();
    }

    public boolean isRestrictedExtension(String ip, FileExtension fileExtension) {
        DownloadInfoRestrict downloadInfoRestrict = find(ip);
        return downloadInfoRestrict.isRestrictedFileExtension(fileExtension);
    }

}
