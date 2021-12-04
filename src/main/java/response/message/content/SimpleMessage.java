package response.message.content;

import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class SimpleMessage extends Message {
  private final String message;

  public SimpleMessage(String message) {
    this.message = message;
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }

  @Override
  protected String getContent() {
    content.append("<h1>").append(message).append("</h1>");
    
    return content.toString();
  }

}
