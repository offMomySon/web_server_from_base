package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.server.BasicConfig;
import config.server.download.DownloadConfig;
import config.server.thread.ThreadConfig;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ConfigManager {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String BASIC_CONFIG_PATH = "src/main/resources/config/basic_setting.json";
  private static final String DOWNLOAD_CONFIG_PATH = "src/main/resources/config/download_setting.json";
  private static final String THREAD_CONFIG_PATH = "src/main/resources/config/thread_setting.json";

  private final BasicConfig basicConfig;
  private final ThreadConfig threadConfig;
  private final DownloadConfig downloadConfig;


  public ConfigManager() throws FileNotFoundException {
    this(new DataInputStream(
            new BufferedInputStream(new FileInputStream(BASIC_CONFIG_PATH))),
        new DataInputStream(
            new BufferedInputStream(new FileInputStream(DOWNLOAD_CONFIG_PATH))),
        new DataInputStream(
            new BufferedInputStream(new FileInputStream(THREAD_CONFIG_PATH))));
  }

  public ConfigManager(DataInputStream dataInputStreamOfBasicConfig,
      DataInputStream dataInputStreamOfDownloadConfig,
      DataInputStream dataInputStreamOfThreadConfig) {

    ObjectMapper objectMapper = new ObjectMapper();
    logger.info("start basicConfig");
    this.basicConfig = createBasicConfig(objectMapper, dataInputStreamOfBasicConfig);
    logger.info("start downloadConfig");
    this.downloadConfig = createDownloadConfig(objectMapper, dataInputStreamOfDownloadConfig);
    logger.info("start createThreadConfig");
    this.threadConfig = createThreadConfig(objectMapper, dataInputStreamOfThreadConfig);
  }

  public boolean isWelcomePageUri(String uri){
    return basicConfig.isWelcomePageUri(uri);
  }

  private static BasicConfig createBasicConfig(ObjectMapper objectMapper,
      DataInputStream dataInputStream) {
    try {
      return objectMapper.readValue((DataInput) dataInputStream, BasicConfig.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static DownloadConfig createDownloadConfig(ObjectMapper objectMapper,
      DataInputStream dataInputStream) {
    try {
      return objectMapper.readValue((DataInput) dataInputStream, DownloadConfig.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static ThreadConfig createThreadConfig(ObjectMapper objectMapper,
      DataInputStream dataInputStream) {
    try {
      return objectMapper.readValue((DataInput) dataInputStream, ThreadConfig.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public int getPort() {
    return basicConfig.getPort();
  }

  public String getWelcomPagePath() {
    return basicConfig.getWelcomPagePath();
  }

  public int getUsableThreadCount() {
    return threadConfig.getUsableThreadCount();
  }

  public int getWaitableThreadCount() {
    return threadConfig.getWaitableThreadCount();
  }

  public int getDownloadCount() {
    return downloadConfig.getCount();
  }

  public String getDownloadPath() {
    return downloadConfig.getDownloadPath();
  }

  public int getDownloadPeriod() {
    return downloadConfig.getPeriod();
  }

  public List<String> getRestrictedFileExtension() {
    return downloadConfig.getRestrictedFileExtension();
  }
}
