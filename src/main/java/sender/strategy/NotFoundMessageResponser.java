package sender.strategy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotFoundMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());
  
  private final OutputStream outputStream;

  public NotFoundMessageResponser(OutputStream outputStream) {
    this.outputStream = new BufferedOutputStream(outputStream);
  }

  @Override
  public void doSend() {
    try {
      logger.info("Start to response file not exist info.");

      String notExistErrMsg = "File/Directory not exist";

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
