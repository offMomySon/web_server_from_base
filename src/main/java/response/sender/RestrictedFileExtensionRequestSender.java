package response.sender;

import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.message.content.SimpleMessage;
import response.message.sender.Message;

@Slf4j
public class RestrictedFileExtensionRequestSender extends RequestSender {
  private final String RESTRICTED_FILE_EXTENSION = "restricted file extension.";

  public void doProcess(HttpRequest httpRequest, OutputStream outputStream) {
    Message message = new SimpleMessage(RESTRICTED_FILE_EXTENSION);
    message.create();
  }
}