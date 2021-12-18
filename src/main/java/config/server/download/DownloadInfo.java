package config.server.download;

import config.ConfigManager;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

@Slf4j
public class DownloadInfo {
    private static final List<DownloadInfo> records = new LinkedList<>();

    // 도메인 객체 필요?
    // inet의 hostAddress 도 리턴값이 string 이니 굳이할 필요 없을것 같다.
    private final String hostAddress;
    private final DownloadRate downloadRate;
    private final TreeSet<Instant> downloadTimes = new TreeSet<>();

    public DownloadInfo(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        this.hostAddress = hostAddress;

        // Todo
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();
        if (downloadConfig.containsDownloadRateInfoAtHostAddress(this.hostAddress)) {
            this.downloadRate = downloadConfig.getDownloadRateInfoAtHostAddress(this.hostAddress).getDownloadRate();
        } else {
            this.downloadRate = downloadConfig.getDownloadRate();
        }
    }

    public static DownloadInfo getDownloadInfoAtHostAddress(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        for (DownloadInfo record : records) {
            if (record.isEqualAtHostAddress(hostAddress)) {
                return record;
            }
        }

        DownloadInfo downloadInfo = new DownloadInfo(hostAddress);
        records.add(downloadInfo);

        return downloadInfo;
    }

    // Todo
    // method name 괜찮은가
    public boolean isEqualAtHostAddress(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        return this.hostAddress.equals(hostAddress);
    }

    public long getDownloadCountPerPeriod() {
        return downloadRate.getCount();
    }

    public long getPeriod() {
        return downloadRate.getPeriod();
    }

    public long leftWaitTimeForDownload() {
        if (downloadRate.getCount() > downloadTimes.size()) {
            return 0;
        }

        removeRequestRecordIfOverPeriod();

        // Todo
        // 연산하는 시간 자료형 통일해야 할거 같은데.
        Instant lastDownloadTime = downloadTimes.first();
        long exclusiveTime = Instant.now().toEpochMilli() - downloadRate.getPeriod();

        return lastDownloadTime.toEpochMilli() - exclusiveTime;
    }

    public boolean isPossibleDownload() {
        log.info("isPossibleDownload called");
        removeRequestRecordIfOverPeriod();

        if (downloadRate.getCount() > downloadTimes.size()) {
            return true;
        }

        return false;
    }

    public void addRequestTime(Instant requestTime) {
        log.info("addRequestTime called");
        if (Objects.isNull(requestTime)) {
            throw new IllegalArgumentException("requestTime 이 null 입니다.");
        }

        removeRequestRecordIfOverPeriod();

        downloadTimes.add(requestTime);
    }

    // 주기적으로 지워 줘야하나?
    private void removeRequestRecordIfOverPeriod() {
        log.info("removeRequestRecordIfOverPeriod called");

        Instant currentTime = Instant.now();

        Iterator<Instant> iter = downloadTimes.iterator();
        while (iter.hasNext()) {
            Instant downloadedTime = iter.next();

            long elapsedTime = currentTime.toEpochMilli() - downloadedTime.toEpochMilli();
            log.info("currentTime - downloadedTime = {}", elapsedTime);

            if (elapsedTime > downloadRate.getPeriod()) {
                log.info("elapsedTime = {} > seekPeriod = {}, so remove", elapsedTime, downloadRate.getPeriod());
                iter.remove();
            }
        }

        log.info("count = {}", downloadRate.getCount());
        log.info("downloadTimes = {}", downloadTimes.size());
    }

}
