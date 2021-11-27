package sender.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleMessageResponser extends MessageResponser {
  private final String message;

  public SimpleMessageResponser(String message) {
    this.message = message;
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }

  @Override
  protected String getContent() {
    log.info("Start to response no thread available.");

    content.append("<h1>").append(message).append("</h1>");

    log.info("Finish to response file not exist info.");
    return content.toString();
  }

}
