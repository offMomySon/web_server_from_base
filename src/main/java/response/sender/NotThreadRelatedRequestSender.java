package response.sender;

import config.ConfigManager;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;
import response.messageFactory.creater.OrderedMessageResponserFactories;

@Slf4j
public class NotThreadRelatedRequestSender extends RequestSender {
  public void doProcess(HttpRequest httpRequest, OutputStream outputStream) {
    AbstractMessageFactory responserFactory = new OrderedMessageResponserFactories(() -> false, ConfigManager.getInstance()).create();
    Message message = responserFactory.createMessage(httpRequest.getRequestTarget());
    message.doSend(outputStream);
  }
}