package resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class ResourceController {

  private static final String RESOURCE_ROOT_PATH = "src/main/resources/files";
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public ResourceStatus getResourceStatus(String filePath) {
    logger.info("Check resource status by filePath[{}]", filePath);
    File file = new File(filePath);

    if (file.exists()) {
      if (file.isDirectory()) {
        logger.info("File Status : directory exist");
        return ResourceStatus.DIRECTORY_EXIST;
      } else if (file.isFile()) {
        logger.info("File Status : file exist");
        return ResourceStatus.FILE_EXIST;
      }
    }

    logger.info("File not exist at filePath[{}]", filePath);
    return ResourceStatus.PATH_NOT_EXIST;
  }

  public String getFilePath(String httpRequestMessage) {
    logger.debug("Try to extract url path from http request header.");

    String path = httpRequestMessage.split(" ")[1];
    logger.debug("URL PATH : {}", path);

    return RESOURCE_ROOT_PATH + path;
  }
}
