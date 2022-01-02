package config.server.download;

import config.ConfigManager;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DownloadInfo {
    private final DownloadRate downloadRate;
    private final List<Long> downloadTimes = new LinkedList<>();

    private DownloadInfo(DownloadRate downloadRate) {
        this.downloadRate = downloadRate;
    }

    public static DownloadInfo create(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        DownloadRate downloadRate = null;

        //TODO config 없을때도 고려해야함.
        // config 없을 때 어떻게 json creater 동작할지 모르겠음 지금은.
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
        if (downloadConfig.containsDownloadRateInfoAtHostAddress(hostAddress)) {
            downloadRate = downloadConfig.getDownloadRateInfoAtHostAddress(hostAddress).getDownloadRate();
        } else {
            downloadRate = downloadConfig.getDownloadRate();
        }

        return new DownloadInfo(downloadRate);
    }

    public long getDownloadCountPerPeriod() {
        return downloadRate.getCount();
    }

    public long getPeriod() {
        return downloadRate.getPeriod();
    }

    public long leftWaitTimeForDownload() {
        if (downloadTimes.size() <= 0) {
            return 0;
        }
        if (!downloadRate.isExceedDownloadCount(downloadTimes.size())) {
            return 0;
        }

        long exclusiveTime = Instant.now().toEpochMilli() - downloadRate.getPeriod();
        log.info("exclusiveTime = {}", exclusiveTime);
        log.info("downloadTime = {}", downloadTimes.get(0));

        return downloadTimes.get(0) - exclusiveTime;
    }

    public boolean isPossibleDownload() {
        log.info("isPossibleDownload called");

        if (downloadRate.isExceedDownloadCount(downloadTimes.size())) {
            return true;
        }

        return false;
    }

    public void addRequestTime(long requestTime) {
        log.info("addRequestTime called");

        downloadTimes.add(requestTime);
    }

    public void removeRequestRecordIfOverPeriod() {
        log.info("removeRequestRecordIfOverPeriod called");

        long currentTime = System.currentTimeMillis();

        Iterator<Long> iter = downloadTimes.iterator();
        while (iter.hasNext()) {
            long downloadedTime = iter.next();

            long elapsedTime = currentTime - downloadedTime;
            log.info("currentTime - downloadedTime = {}", elapsedTime);

            if (elapsedTime > downloadRate.getPeriod()) {
                log.info("elapsedTime = {} > seekPeriod = {}, so remove", elapsedTime, downloadRate.getPeriod());
                iter.remove();
            } else {
                break;
            }
        }

        log.info("count = {}", downloadRate.getCount());
        log.info("downloadTimes = {}", downloadTimes.size());
    }
}
