package sender.strategy;

import config.ConfigManager;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomPageMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;
  private final ConfigManager configManager;

  public WelcomPageMessageResponser(OutputStream outputStream, ConfigManager configManager) {
    this.outputStream = new BufferedOutputStream(outputStream);
    this.configManager = configManager;
  }

  @Override
  public void doSend() {
    logger.info("Start to response welcompage, config");

    StringBuilder welcomPageBuilder = new StringBuilder();

    welcomPageBuilder.append("Server port : ").append(configManager.getPort()).append("</br>");
    welcomPageBuilder.append("welcome page path : ").append(configManager.getWelcomPagePath())
        .append("</br>");
    welcomPageBuilder.append("usable thread count : ").append(configManager.getUsableThreadCount())
        .append("</br>");
    welcomPageBuilder.append("waitable thread count : ")
        .append(configManager.getWaitableThreadCount()).append("</br>");
    welcomPageBuilder.append("download path : ").append(configManager.getDownloadPath())
        .append("</br>");
    welcomPageBuilder.append("download count : ").append(configManager.getDownloadCount())
        .append("</br>");
    welcomPageBuilder.append("download period : ").append(configManager.getDownloadPeriod())
        .append("</br>");
    welcomPageBuilder.append("restricted file extension : ")
        .append(configManager.getRestrictedFileExtension().toString()).append("</br>");

    try {
      String responseMsg = createHeader(welcomPageBuilder.toString().length(),
          DEFAULT_CONTENT_TYPE);
      responseMsg += welcomPageBuilder + "\r\n";

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response directory info.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
