package reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IoUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

public class MessageReader {

  private static final char CR = '\r';
  private static final char LF = '\n';
  private static final String FINISH_READ_REQUEST_MESSAGE = "\r\n\r\n";
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final InputStream inputStream;
  private final byte[] buffer;
  private final StringBuilder body = new StringBuilder();

  public MessageReader(InputStream inputStream) {
    this.inputStream = new BufferedInputStream(inputStream);
    buffer = new byte[8192];
  }

  public String getHttpRequestHeader() throws IOException {
    logger.info("Start to read Http request header.");

    int readNo = -1;
    StringBuilder httpRequestMessage = new StringBuilder();

    while ((readNo = inputStream.read(buffer)) != -1) {
      logger.info("Reading http request message...");

      httpRequestMessage.append(new String(buffer, 0, readNo, StandardCharsets.UTF_8));

      if (isEndOfHeader(httpRequestMessage)) {
        logger.info("Finish read http request message.");
        break;
      }
    }
    return httpRequestMessage.toString();
  }

  private boolean isEndOfHeader(StringBuilder httpRequestMessage) {
    logger.debug("Start checking for end of header");

    int msgLength = httpRequestMessage.length();

    return msgLength >= 4 && (httpRequestMessage.charAt(msgLength - 4) == CR &&
        httpRequestMessage.charAt(msgLength - 3) == LF &&
        httpRequestMessage.charAt(msgLength - 2) == CR &&
        httpRequestMessage.charAt(msgLength - 1) == LF);
  }

}
