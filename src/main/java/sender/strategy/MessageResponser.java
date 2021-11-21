package sender.strategy;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageResponser {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String createHeader(long contentLength ) {
    return "HTTP/1.1 200 OK \r\n" +
        "Content-Length : " + contentLength + "\r\n" +
        "Content-Type: " + getContentType() + "\r\n" +
        "Date: " + new Date() + "\r\n" +
        "Server: jihun server 1.0 \r\n" +
        "\r\n";
  }

  protected abstract String createContent();

  protected abstract String getContentType();

  public final void doSend(OutputStream outputStream){
    logger.info("Start to response exist file.");

    try {
      String content = createContent();
      // byte 의 lenth 해야함.
      String responseMsg = createHeader(content.length());

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));

      outputStream.write(content.getBytes(StandardCharsets.UTF_8));

      outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response exit file.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  };
}
