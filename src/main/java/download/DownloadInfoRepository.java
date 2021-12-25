package download;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DownloadInfoRepository {
    private static final DownloadInfoRepository INSTANCE = new DownloadInfoRepository();

    private final Map<String, DownloadInfo> downloadInfos = new HashMap<>();

    private DownloadInfoRepository() {
        Thread thread = new Thread(() -> {
            while (true) {
                downloadInfos.values().forEach(DownloadInfo::removeRequestRecordIfOverPeriod);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(
                () -> downloadInfos.values().forEach(DownloadInfo::removeRequestRecordIfOverPeriod),
                0, Duration.ofSeconds(1).getSeconds(), TimeUnit.SECONDS);


        CompletableFuture.runAsync(() -> {
            System.out.println("a");
        }, scheduledExecutorService);
    }

    public static DownloadInfoRepository getInstance() {
        return INSTANCE;
    }

    public DownloadInfo createIfAbsent(String hostAddress) {
        return downloadInfos.computeIfAbsent(hostAddress, DownloadInfo::createDownloadInfo);
    }


}
