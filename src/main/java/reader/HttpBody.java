package reader;

import static util.IoUtil.*;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IoUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class HttpBody {

  private final StringBuilder body = new StringBuilder();
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public HttpBody(BufferedReader reader) {
    String bodyLine = null;
    try {
      logger.info("ready to get http body ");
      while ((bodyLine = reader.readLine()) != null) {
        logger.info("loop.. get http body");
        body.append(bodyLine);
      }
      logger.info("reult body == \n" + body.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getBody() {
    return body.toString();
  }
}
