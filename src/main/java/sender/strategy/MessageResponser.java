package sender.strategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageResponser {
  private static final String END_OF_LINE = "\r\n";

  private final StringBuilder headerBuilder = new StringBuilder();
  protected final StringBuilder content = new StringBuilder();

  private String createHeader(long contentLength) {
    headerBuilder.append("HTTP/1.1 200 OK ").append(END_OF_LINE);
    headerBuilder.append("Content-Length : ").append(contentLength).append(END_OF_LINE);
    headerBuilder.append("Content-Type: ").append(getContentType()).append(END_OF_LINE);
    headerBuilder.append("Date: ").append(new Date()).append(END_OF_LINE);
    headerBuilder.append("Server: jihun server 1.0 ").append(END_OF_LINE);
    headerBuilder.append(END_OF_LINE);

    return headerBuilder.toString();
  }

  public void doSend(OutputStream outputStream) {
    try {
      String content = getContent();
      String header = createHeader(content.length());

      outputStream.write(header.getBytes(StandardCharsets.UTF_8));
      outputStream.write(content.getBytes(StandardCharsets.UTF_8));
      outputStream.write(END_OF_LINE.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract String getContentType();

  protected abstract String getContent();
}
