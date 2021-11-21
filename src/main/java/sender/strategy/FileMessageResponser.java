package sender.strategy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMessageResponser extends MessageResponser {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final String filePath;
  private final char[] buffers = new char[4096];

  public FileMessageResponser( String filePath) {
    this.filePath = filePath;
  }

  @Override
  protected String getContentType() {
    logger.info("Guess content type.");

    if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
      return "text/html";
    } else if (filePath.endsWith(".txt") || filePath.endsWith(".java")) {
      return "text/plain";
    } else if (filePath.endsWith(".gif")) {
      return "image/gif";
    } else if (filePath.endsWith(".png")) {
      return "image/png";
    } else if (filePath.endsWith(".class")) {
      return "application/octet-stream";
    } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
      return "image/jpeg";
    } else if (filePath.endsWith(".mpeg")) {
      return "video/mpeg";
    } else if (filePath.endsWith(".ts")) {
      return "video/MP2T";
    } else if (filePath.endsWith(".avi")) {
      return "video/x-msvideo";
    } else {
      return "text/plain";
    }
  }

  @Override
  protected String createContent() {
    StringBuilder stringBuilder = new StringBuilder();

    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));) {
      int readNo = 0;

      while(( readNo = bufferedReader.read(buffers)) !=  -1 ){
        stringBuilder.append(buffers,0, readNo);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return stringBuilder.toString();
  }

}
