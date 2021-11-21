package sender;

import config.ConfigManager;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sender.factory.thread.ThreadStatus;

public class MessageManager {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ConfigManager configmanager;

  public MessageManager(ConfigManager configManager) {
    this.configmanager = configManager;
  }

  public void sendMessage(String filePath, ThreadStatus threadStatus)
      throws IOException {
//    PathMessageResponserFactory pathMessageResponserFactory = new PathMessageResponserFactory(filePath, configmanager);
//
//    MessageResponser messageResponser = pathMessageResponserFactory.createMessageResponser();

//    messageResponser.doSend(outputStream);
//
//    AbstractMessageResponserFactory factory = new OrderedMessageResponserFactories(configmanager).create();
//
//    factory.createMessageResponser(filePath);
  }


}
