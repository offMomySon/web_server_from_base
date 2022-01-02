package config.server.download;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DownloadInfoRepository {
    private static final DownloadInfoRepository INSTANCE = new DownloadInfoRepository();

    private static final Map<String, DownloadInfo> downloadInfos = new HashMap<>();

    private DownloadInfoRepository() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> downloadInfos.values().stream().forEach(DownloadInfo::removeRequestRecordIfOverPeriod),
                0, 0, TimeUnit.MILLISECONDS);
    }

    public static boolean contains(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        return downloadInfos.containsKey(hostAddress);
    }

    public static DownloadInfo addDownloadInfo(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        return downloadInfos.put(hostAddress, DownloadInfo.create(hostAddress));
    }

    public static DownloadInfo getDownloadInfo(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        return downloadInfos.get(hostAddress);
    }

    public DownloadInfoRepository getInstance() {
        return INSTANCE;
    }
}
