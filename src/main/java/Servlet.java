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

  public Servlet(InputStream inputStream, OutputStream outputStream,
      ResourceController resourceController, ConfigManager configManager) {
    this.resourceController = resourceController;
    this.httpRequest = new HttpRequest(inputStream);
    this.messageManager = new MessageManager(outputStream, configManager);
  }

  @Override
  public void run() {
    processRequest();
  }

  private void processRequest() {
    try {
      String requestTarget = httpRequest.getRequestTarget();

      String filePath = resourceController.getFilePath(requestTarget);
      ResourceStatus resourceStatus = resourceController.getResourceStatus(requestTarget);

      messageManager.sendMessage(filePath, resourceStatus);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}