package sender.strategy;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MessageResponser {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String DEFAULT_CONTENT_TYPE = "text/html";

  protected String createHeader(long contentLength, String contentType) {
    return "HTTP/1.1 200 OK \r\n" +
        "Content-Length : " + contentLength + "\r\n" +
        "Content-Type: " + contentType + "\r\n" +
        "Date: " + new Date() + "\r\n" +
        "Server: jihun server 1.0 \r\n" +
        "\r\n";
  }

  protected String getContentType(String path) {
    logger.info("Guess content type.");

    if (path.endsWith(".html") || path.endsWith(".htm")) {
      return "text/html";
    } else if (path.endsWith(".txt") || path.endsWith(".java")) {
      return "text/plain";
    } else if (path.endsWith(".gif")) {
      return "image/gif";
    } else if (path.endsWith(".png")) {
      return "image/png";
    } else if (path.endsWith(".class")) {
      return "application/octet-stream";
    } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
      return "image/jpeg";
    } else if (path.endsWith(".mpeg")) {
      return "video/mpeg";
    } else if (path.endsWith(".ts")) {
      return "video/MP2T";
    } else if (path.endsWith(".avi")) {
      return "video/x-msvideo";
    } else {
      return "text/plain";
    }
  }

  public abstract void doSend();
}
