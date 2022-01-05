package config.server.download2;

import domain.FileExtension;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

public class DownloadInfoRestrict {
    private final long count;
    private final Set<FileExtension> fileExtensions;

    public DownloadInfoRestrict(long count, @NonNull Set<FileExtension> fileExtensions) {
        this.count = validatePositive(count);
        this.fileExtensions = Collections.unmodifiableSet(fileExtensions);
    }

    private static long validatePositive(long count) {
        return count;
    }

    public static DownloadInfoRestrict from(DownloadInfoAtHostAddress downloadInfoAtHostAddress) {

        DownloadRate2 downloadRate = downloadInfoAtHostAddress.getDownloadRate();
        Set<FileExtension> fileExtensions = downloadInfoAtHostAddress.getFileExtensions();

        return new DownloadInfoRestrict(downloadRate.getCount(), fileExtensions);
    }

    public DownloadInfoRestrict increaseCount() {
        return new DownloadInfoRestrict(count + 1, fileExtensions);
    }

    public DownloadInfoRestrict decreaseCount() {
        return new DownloadInfoRestrict(count - 1, fileExtensions);
    }

    public boolean isResctrictedCount() {
        return count == 0;
    }

    public boolean isRestrictedFileExtension(FileExtension fileExtension) {
        return false;
    }
}
