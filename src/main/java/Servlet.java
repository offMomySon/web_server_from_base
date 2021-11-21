import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.HttpRequest;
import sender.factory.AbstractMessageResponserFactory;
import sender.strategy.MessageResponser;

public class Servlet {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final HttpRequest httpRequest;
  private final OutputStream outputStream;
  private final AbstractMessageResponserFactory factory;

  public Servlet(InputStream inputStream, OutputStream outputStream, AbstractMessageResponserFactory factory) {
    this.outputStream = outputStream;
    this.factory = factory;
    this.httpRequest = new HttpRequest(inputStream);
  }

  public void processRequest() {
    String requestTarget = httpRequest.getRequestTarget();

    MessageResponser messageResponser = factory.createMessageResponser(requestTarget);
    messageResponser.doSend(outputStream);
  }
}