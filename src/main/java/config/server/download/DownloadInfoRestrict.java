package config.server.download;

import config.server.download.data.DownloadInfoAtIp;
import domain.FileExtension;

import java.util.Collections;
import java.util.Set;

public class DownloadInfoRestrict {
    private final long count;
    private final Set<FileExtension> restrictedFileExtensions;

    private DownloadInfoRestrict(long count, Set<FileExtension> restrictedFileExtensions) {
        this.count = validatePositive(count);
        this.restrictedFileExtensions = Collections.unmodifiableSet(restrictedFileExtensions);
    }

    public static DownloadInfoRestrict from(DownloadInfoAtIp downloadInfoAtIp) {
        return new DownloadInfoRestrict(downloadInfoAtIp.getDownloadRate().getCount(), downloadInfoAtIp.getRestrictedFileExtensions());
    }

    private long validatePositive(long count) {
        if (count < 0) {
            throw new IllegalArgumentException("count 는 0 미만일 수 없습니다.");
        }
        return count;
    }

    public DownloadInfoRestrict increaseCount() {
        return new DownloadInfoRestrict(count + 1, restrictedFileExtensions);
    }

    public DownloadInfoRestrict decreaseCount() {
        return new DownloadInfoRestrict(count - 1, restrictedFileExtensions);
    }

    public boolean isRestrictedCount() {
        return count <= 0;
    }

    public boolean isRestrictedFileExtension(FileExtension fileExtension) {
        return restrictedFileExtensions.contains(fileExtension);
    }
}
