package response.message.content;

import config.server.download.data.DownloadInfoAtIp;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class ExceedDownloadMessage extends Message {
    private final DownloadInfoAtIp downloadInfoAtIp;

    public ExceedDownloadMessage(DownloadInfoAtIp downloadInfoAtIp) {
        this.downloadInfoAtIp = downloadInfoAtIp;
    }

    @Override
    protected String getContentType() {
        return "text/html";
    }

    @Override
    protected String getContent() {
        log.info("Start to make ExceedDownloadMessage");

        //Todo 5
        // checker 에 있는 DownloadInfoRestrict 에 left 시간을 기록하지 않고 있음.
//        long downloadCountPerPeriod = downloadInfoAtIp.getDownloadCountPerPeriod();
        long period = downloadInfoAtIp.getDownloadRate().getPeriod().getSeconds();
//        long leftWaitTimeForDownload = downloadInfo.leftWaitTimeForDownload();

        content.append("Exceed download count!!").append("</br>");
        content.append("Period : ").append(period).append("</br>");
//        content.append("DownloadCountPerPeriod : ").append(downloadCountPerPeriod).append("</br>");
//        content.append("LeftWaitTimeForDownload : ").append(leftWaitTimeForDownload).append(" ms").append("</br>");

        return content.toString();
    }
}
