package reader;

import httpspec.HttpMethod;
import java.lang.invoke.MethodHandles;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static util.IoUtil.*;

@Getter
public class HttpStartLine {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final HttpMethod httpMethod;
  private final String requestTarget;
  private final String httpVersion;

  public HttpStartLine(BufferedReader reader) {
    try {
      logger.info("Read get startLine");
      String startLine = reader.readLine();
      logger.info("StartLine : " + startLine);

      httpMethod = HttpMethod.valueOf(startLine.split(" ")[0]);
      requestTarget = startLine.split(" ")[1];
      httpVersion = startLine.split(" ")[2];

      logger.debug("HttpMethod : " + httpMethod);
      logger.debug("RequestTarget : " + requestTarget);
      logger.debug("HttpVersion : " + httpVersion);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
