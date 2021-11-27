package sender.strategy;

import config.ConfigManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WelcomPageMessageResponser extends MessageResponser {
  private final ConfigManager configManager;

  public WelcomPageMessageResponser(ConfigManager configManager) {
    this.configManager = configManager;
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }

  @Override
  protected String getContent() {
    log.info("Start to response welcompage, config");

    content.append("Server port : ").append(configManager.getPort()).append("</br>");
    content.append("welcome page path : ").append(configManager.getWelcomPagePath()).append("</br>");
    content.append("usable thread count : ").append(configManager.getUsableThreadCount()).append("</br>");
    content.append("waitable thread count : ").append(configManager.getWaitableThreadCount()).append("</br>");
    content.append("download path : ").append(configManager.getDownloadPath()).append("</br>");
    content.append("download count : ").append(configManager.getDownloadCount()).append("</br>");
    content.append("download period : ").append(configManager.getDownloadPeriod()).append("</br>");
    content.append("restricted file extension : ").append(configManager.getRestrictedFileExtension().toString()).append("</br>");

    return content.toString();
  }
}
