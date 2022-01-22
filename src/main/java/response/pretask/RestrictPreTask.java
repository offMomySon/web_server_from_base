package response.pretask;

import config.ConfigManager;
import config.server.download.DownloadInfoRestrictChecker;
import domain.FileExtension;
import lombok.NonNull;

public class RestrictPreTask extends FileRequestPreTask {

    private RestrictPreTask(Runnable task) {
        super(task);
    }

    public static RestrictPreTask create(@NonNull String hostAddress,
                                         @NonNull FileExtension fileExtension) {

        DownloadInfoRestrictChecker checker = ConfigManager.getInstance().getDownloadConfig().getDownloadInfoRestrictChecker();

        return new RestrictPreTask(() -> {
            if (checker.isRestricted(hostAddress, fileExtension)) {
                return;
            }

            checker.useResource(hostAddress);
        });
    }
}
