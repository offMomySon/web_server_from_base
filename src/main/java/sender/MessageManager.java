package sender;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.ResourceStatus;
import sender.factory.MessageResponserFactory;
import sender.strategy.MessageResponser;

public class MessageManager {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;

  public MessageManager(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public void sendMessage(String filePath, ResourceStatus resourceStatus) throws IOException {

    MessageResponser messageResponser = MessageResponserFactory
        .getMessageResponser(outputStream, resourceStatus, filePath);

    messageResponser.doSend();
  }


}
