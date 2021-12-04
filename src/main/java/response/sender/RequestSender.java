package response.sender;

import java.io.OutputStream;
import reader.httpspec.HttpRequest;

public abstract class RequestSender {
  public abstract void doProcess(HttpRequest httpRequest, OutputStream outputStream);
}
