package sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.ResourceStatus;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

public class MessageSender {

  private static final String DEFAULT_CONTENT_TYPE = "text/html";
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final OutputStream outputStream;

  public MessageSender(OutputStream outputStream) {
    this.outputStream = new BufferedOutputStream(outputStream);
  }

  public void sendResponse(String filePath, ResourceStatus resourceStatus) throws IOException {
    switch (resourceStatus) {
      case FILE_EXIST:
        sendExistFileResponse(filePath);
        break;
      case DIRECTORY_EXIST:
        sendDirectoryResponse(filePath);
        break;
      case PATH_NOT_EXIST:
        sendFileNotExistResponse();
    }
  }

  private void sendExistFileResponse(String filePath) {
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

  private void sendDirectoryResponse(String filePath) {
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

  private void sendFileNotExistResponse() {
    try {
      logger.info("Start to response file not exist info.");

      String notExistErrMsg = "File/Directory not exist";

      String responseMsg = createHeader(notExistErrMsg.length(), DEFAULT_CONTENT_TYPE);
      responseMsg += notExistErrMsg + "\r\n";

      outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      logger.info("Finish to response file not exist info.");
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

  private String createHeader(long contentLength, String contentType) {
    return "HTTP/1.1 200 OK \r\n" +
        "Content-Length : " + contentLength + "\r\n" +
        "Content-Type: " + contentType + "\r\n" +
        "Date: " + new Date() + "\r\n" +
        "Server: jihun server 1.0 \r\n" +
        "\r\n";
  }

  private String getContentType(String path) {
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
}
