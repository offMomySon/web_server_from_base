package sender.strategy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;
  private final String filePath;

  public FileMessageResponser(OutputStream outputStream, String filePath) {
    this.outputStream = new BufferedOutputStream(outputStream);
    this.filePath = filePath;
  }

  @Override
  public void doSend() {
    logger.info("Start to response exist file.");

    try {
      File file = new File(filePath);
      String responseMsg = createHeader(file.length(), getContentType(filePath));

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));

      outputFile(filePath, outputStream);

      outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response exit file.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void outputFile(String filePath, OutputStream outputStream) {
    logger.info("Start to response file at http body.");

    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      byte[] buffer = new byte[8192];
      int readNo = -1;

      while ((readNo = fileInputStream.read(buffer)) != -1) {
        logger.debug("File binary read looping.. path[{}]", filePath);
        outputStream.write(buffer, 0, readNo);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    logger.info("Finish readFile.");
  }
}
