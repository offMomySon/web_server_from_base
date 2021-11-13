package sender.strategy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;
  private final String filePath;

  public DirectoryMessageResponser(OutputStream outputStream, String filePath) {
    this.outputStream = new BufferedOutputStream(outputStream);
    this.filePath = filePath;
  }

  @Override
  public void doSend() {
    logger.info("Start to response directory info.");
    File file = new File(filePath);

    StringBuilder curDirFilesBuilder = new StringBuilder();
    curDirFilesBuilder.append("this is file/dir list</br>");
    for (File curDirFile : Objects.requireNonNull(file.listFiles())) {
      curDirFilesBuilder.append("/").append(curDirFile.getName()).append("</br>");
    }
    logger.info("Result of file/dir list [{}]", curDirFilesBuilder.toString());

    try {
      String responseMsg = createHeader(curDirFilesBuilder.toString().length(),
          DEFAULT_CONTENT_TYPE);
      responseMsg += curDirFilesBuilder + "\r\n";

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response directory info.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
