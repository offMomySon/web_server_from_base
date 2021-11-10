import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.HttpRequest;
import resource.ResourceController;
import resource.ResourceStatus;
import sender.MessageSender;

public class Servlet implements Runnable {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ResourceController resourceController;
  private final HttpRequest httpRequest;
  private final MessageSender messageSender;

  public Servlet(InputStream inputStream, OutputStream outputStream,
      ResourceController resourceController) {
    this.resourceController = resourceController;
    this.httpRequest = new HttpRequest(inputStream);
    this.messageSender = new MessageSender(outputStream);
  }

  @Override
  public void run() {
    try {
      String requestTarget = httpRequest.getRequestTarget();
      String filePath = resourceController.getFilePath(requestTarget);

      ResourceStatus resourceStatus = resourceController.getResourceStatus(filePath);

      messageSender.sendResponse(filePath, resourceStatus);
    } catch (IOException e) {
      logger.error("Exception happen", e);
      e.printStackTrace();
    }
  }
}