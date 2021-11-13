package sender.strategy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoThreadAvailableMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;

  public NoThreadAvailableMessageResponser(OutputStream outputStream) {
    this.outputStream = new BufferedOutputStream(outputStream);
  }

  @Override
  public void doSend() {
    try {
      logger.info("Start to response no thread available.");

      String notExistErrMsg = "There are no threads available.</br>"
          + "Please try again later";

      String responseMsg = createHeader(notExistErrMsg.length(), DEFAULT_CONTENT_TYPE);
      responseMsg += notExistErrMsg + "\r\n";

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response file not exist info.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
