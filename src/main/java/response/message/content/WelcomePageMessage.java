package response.message.content;

import config.ConfigManager;
import config.server.BasicConfig;
import config.server.download.data.DownloadConfig;
import config.server.thread.ThreadConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class WelcomePageMessage extends Message {
    private final ConfigManager configManager;

    public WelcomePageMessage(@NonNull ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    protected String getContentType() {
        return "text/html";
    }

    @Override
    protected String getContent() {
        log.info("Start to response welcome, config");

        ThreadConfig threadConfig = configManager.getThreadConfig();
        BasicConfig basicConfig = configManager.getBasicConfig();
        DownloadConfig downloadConfig = configManager.getDownloadConfig();

        content.append("Server port : ").append(basicConfig.getPort()).append("</br>");
        content.append("welcome page path : ").append(basicConfig.getWelcomePageURI()).append("</br>");
        content.append("usable thread count : ").append(threadConfig.getUsableThreadCount()).append("</br>");
        content.append("waitable thread count : ").append(threadConfig.getWaitableThreadCount()).append("</br>");
        content.append("download path : ").append(downloadConfig.getRootPath()).append("</br>");
        content.append("download count : ").append(downloadConfig.getDownloadInfoRepository().getCommonIp().getDownloadRate().getCount()).append("</br>");
        content.append("download period : ").append(downloadConfig.getDownloadInfoRepository().getCommonIp().getDownloadRate().getPeriod().getSeconds()).append("</br>");
        content.append("restricted file extension : ").append(downloadConfig.getDownloadInfoRepository().getCommonIp().getRestrictedFileExtensions().toString()).append("</br>");

        return content.toString();
    }
}
