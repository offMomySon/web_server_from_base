package resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class ResourceController {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final String resourceRootPath;

  public ResourceController(String resourceRootPath) {
    this.resourceRootPath = resourceRootPath;
  }

  public ResourceStatus getResourceStatus(String requestTarget) {
    logger.info("Check resource status by requestTarget[{}]", requestTarget);

    String filePath = getFilePath(requestTarget);

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

  public String getFilePath(String requestTarget) {
    return resourceRootPath + requestTarget;
  }
}
