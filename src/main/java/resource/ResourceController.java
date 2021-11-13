package resource;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Stack;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceController {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final String resourceRootPath;
  private String filePath;

  public ResourceController(String resourceRootPath) {
    this.resourceRootPath = resourceRootPath;
  }

  public ResourceStatus getResourceStatus(String requestTarget) {
    logger.info("Check resource status by requestTarget[{}]", requestTarget);

    File file = new File(getFilePath(requestTarget));

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

  private String modifyRequestTarget(String requestTarget) {
    requestTarget = requestTarget.replaceAll("//", "/");
    requestTarget = shortenRequestTarget(requestTarget);

    return requestTarget;
  }

  private String shortenRequestTarget(String requestTarget) {
    Stack<String> pathStack = new Stack<>();
    String[] splitPath = requestTarget.split("/");

    for (String path : splitPath) {
      if (path.length() == 0) {
        continue;
      }

      if (path.equals("..")) {
        if (pathStack.isEmpty()) {
          throw new RuntimeException("wrong request target.");
        }
        pathStack.pop();
      } else {
        pathStack.push(path);
      }
    }

    return pathStack.stream().collect(Collectors.joining("/", "/", ""));
  }

  public String getFilePath(String requestTarget) {
    if (filePath != null) {
      return filePath;
    }

    filePath = resourceRootPath + modifyRequestTarget(requestTarget);
    return filePath;
  }
}
