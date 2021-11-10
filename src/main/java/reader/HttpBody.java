package reader;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpBody {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final StringBuilder bodyStringBuilder = new StringBuilder();
  private final String body;
  private final char[] charBuffer = new char[8192];

  public HttpBody(BufferedReader reader, int contentLength) {
    try {
      int curContentLength = 0;
      int bytesRead = -1;

      logger.info("Ready to get http body ");
      while ((bytesRead = reader.read(charBuffer)) > 0) {
        logger.debug("Loop.. get http body");

        bodyStringBuilder.append(charBuffer, 0, bytesRead);

        curContentLength += bytesRead;
        logger.debug("Current content length : " + curContentLength);

        if (contentLength == bytesRead) {
          break;
        }
      }

      body = bodyStringBuilder.toString();
      logger.info("End http body read \n == Result body == \n" + body);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getBody() {
    return bodyStringBuilder.toString();
  }
}
