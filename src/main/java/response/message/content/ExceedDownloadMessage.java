package response.message.content;

import download.DownloadInfo;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class ExceedDownloadMessage extends Message {
    private final DownloadInfo downloadInfo;

    public ExceedDownloadMessage(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    @Override
    protected String getContentType() {
        return "text/html";
    }

    @Override
    protected String getContent() {
        log.info("Start to make ExceedDownloadMessage");

        long downloadCountPerPeriod = downloadInfo.getDownloadCountPerPeriod();
        long period = downloadInfo.getPeriod();
        long leftWaitTimeForDownload = downloadInfo.getLeftWaitTimeForDownload();

        content.append("Exceed download count!!").append("</br>");
        content.append("Period : ").append(period).append("</br>");
        content.append("DownloadCountPerPeriod : ").append(downloadCountPerPeriod).append("</br>");
        content.append("LeftWaitTimeForDownload : ").append(leftWaitTimeForDownload).append(" ms").append("</br>");

        return content.toString();
    }
}
