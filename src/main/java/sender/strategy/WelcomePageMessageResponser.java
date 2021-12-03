package sender.strategy;

import config.ConfigManager;
import config.server.BasicConfig;
import config.server.download.DownloadConfig;
import config.server.thread.ThreadConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WelcomePageMessageResponser extends MessageResponser {
  private final ConfigManager configManager;

  public WelcomePageMessageResponser(ConfigManager configManager) {
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
    content.append("welcome page path : ").append(basicConfig.getWelcomePagePath()).append("</br>");
    content.append("usable thread count : ").append(threadConfig.getUsableThreadCount()).append("</br>");
    content.append("waitable thread count : ").append(threadConfig.getWaitableThreadCount()).append("</br>");
    content.append("download path : ").append(downloadConfig.getDownloadPath()).append("</br>");
    content.append("download count : ").append(downloadConfig.getCount()).append("</br>");
    content.append("download period : ").append(downloadConfig.getPeriod()).append("</br>");
    content.append("restricted file extension : ").append(downloadConfig.getRestrictedFileExtension().toString()).append("</br>");

    return content.toString();
  }
}