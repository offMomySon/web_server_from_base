package time;

import config.ConfigManager;
import config.server.download.DownloadConfig;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.TreeSet;

@Slf4j
public class DownloadInfoAtIp {
    // 도메인 객체 필요? 하는게 좋다고 하긴하는데. hostAddress 로 리턴값이 string 이니 굳이할 필요 없을것 같다.
    private final String hostAddress;
    //    private final long seekPeriod;
//    private final int limitCount;
    private final TreeSet<Instant> downloadTimes = new TreeSet<>();

    public DownloadInfoAtIp(String hostAddress) {
        DownloadConfig downloadConfig = ConfigManager.getInstance().getDownloadConfig();

        this.hostAddress = hostAddress;


//        seekPeriod = downloadConfig.getPeriod();
//
//        limitCount = downloadConfig.getCount();
    }

    public boolean isPossibleDownload() {
        removeEntityIfOverPeriod();

        int count = ConfigManager.getInstance().getDownloadConfig().getDownloadRate().getCount();
        log.info("downloadTimes = {}", downloadTimes.size());

        if (count > downloadTimes.size()) {
            return true;
        }

        return false;
    }

    private void removeEntityIfOverPeriod() {
        long seekPeriod = ConfigManager.getInstance().getDownloadConfig().getDownloadRate().getPeriod();

        log.info("seekPeriod = {}", seekPeriod);
        Instant currentTime = Instant.now();

        Iterator<Instant> iter = downloadTimes.iterator();
        while (iter.hasNext()) {
            Instant downloadTime = iter.next();
            log.info("curretTime - downloadTime = {}", currentTime.toEpochMilli() - downloadTime.toEpochMilli());
            Duration between = Duration.between(downloadTime, currentTime);

            long durationInMs = between.toMillis();
            log.info("durationInMs = {}", durationInMs);
            log.info("seekPeriod.toEpochMilli() = {}", seekPeriod);

            if (durationInMs > seekPeriod) {
                iter.remove();
            }
        }
    }

    public void addRequestTime(Instant instant) {
        removeEntityIfOverPeriod();

        downloadTimes.add(instant);
    }

}
