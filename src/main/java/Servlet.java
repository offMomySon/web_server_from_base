import config.ConfigManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.HttpRequest;
import resource.ResourceController;
import resource.ResourceStatus;
import sender.MessageManager;

public class Servlet implements Runnable {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ResourceController resourceController;
  private final HttpRequest httpRequest;
  private final MessageManager messageManager;
  private final ThreadController threadController;
  private final OutputStream outputStream;

  public Servlet(InputStream inputStream, OutputStream outputStream,
      ConfigManager configManager, MessageManager messageManager,
      ThreadController threadController) {
    this.resourceController = createResourceManager(configManager.getDownloadPath(),
        configManager.getWelcomPagePath());
    this.messageManager = messageManager;
    this.outputStream = outputStream;
    this.threadController = threadController;

    this.httpRequest = new HttpRequest(inputStream);
  }

  private static ResourceController createResourceManager(String resourceRootPath,
      String welcomPagePath) {
    return new ResourceController(resourceRootPath, welcomPagePath);
  }

  @Override
  public void run() {
    threadController.waitUntilProcessable();

    processRequest();

    threadController.decreaseUsableThread();
  }

  private void processRequest() {
    try {
      String requestTarget = httpRequest.getRequestTarget();

      String filePath = resourceController.getFilePath(requestTarget);
      ResourceStatus resourceStatus = resourceController.getResourceStatus(requestTarget);

      messageManager.sendMessage(filePath, resourceStatus, outputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}