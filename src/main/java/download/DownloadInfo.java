package download;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import config.server.download.DownloadRate;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

@Slf4j
public class DownloadInfo {
    private static final List<DownloadInfo> records = new LinkedList<>();

    // TODO 도메인 객체 필요?
    // inet의 hostAddress 도 리턴값이 string 이니 굳이할 필요 없을것 같다.
    private final String hostAddress;
    private final DownloadRate downloadRate;

    // 자료구조 변경필요.
    private final TreeSet<Instant> downloadTimes = new TreeSet<>();

    public DownloadInfo(String hostAddress, DownloadRate downloadRate) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }
        if (Objects.isNull(downloadRate)) {
            throw new IllegalArgumentException("downloadRate 가 null 입니다.");
        }

        this.hostAddress = hostAddress;
        this.downloadRate = downloadRate;
    }

    public static DownloadInfo createDownloadInfo(String hostAddress) {
        if (Objects.isNull(hostAddress)) {
            throw new IllegalArgumentException("hostAddress 가 null 입니다.");
        }

        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();


        // config 가 없는 상황 고려해야함.
        DownloadRate downloadRate = null;
        if (downloadConfig.containsDownloadRateInfoAtHostAddress(hostAddress)) {
            downloadRate = downloadConfig.getDownloadRateInfoAtHostAddress(hostAddress).getDownloadRate();
        } else {
            downloadRate = downloadConfig.getDownloadRate();
        }

        return new DownloadInfo(hostAddress, downloadRate);
    }


    public long getDownloadCountPerPeriod() {
        return downloadRate.getCount();
    }

    public long getPeriod() {
        return downloadRate.getPeriod();
    }

    // 디테일한 세부 사항 모두 정책이 필요함.
    public long getLeftWaitTimeForDownload() {
        // 책임 넘겨주기
        if (isPossibleDownload()) {
            return 0;
        }

        // Todo
        // 연산하는 시간 자료형 통일해야 할거 같은데.
        Instant lastDownloadTime = downloadTimes.first();
        long exclusiveTime = Instant.now().toEpochMilli() - downloadRate.getPeriod();

        return lastDownloadTime.toEpochMilli() - exclusiveTime;
    }

    public boolean isPossibleDownload() {
        log.info("isPossibleDownload called");

        if (downloadRate.getCount() > downloadTimes.size()) {
            return true;
        }

        return false;
    }

    // 정합성을 맞추는 고려를 하지않았다, 고려해야함.
    public void recordRequestTime(Instant requestTime) {
        log.info("addRequestTime called");
        if (Objects.isNull(requestTime)) {
            throw new IllegalArgumentException("requestTime 이 null 입니다.");
        }

        if (isPossibleDownload()) {
            downloadTimes.add(requestTime);
        }
    }

    // 주기적으로 지워 줘야하나?
    public void removeRequestRecordIfOverPeriod() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadInfo that = (DownloadInfo) o;
        return hostAddress.equals(that.hostAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostAddress);
    }
}
