package response.message.content;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class DirectoryMessage extends Message {
  private final String filePath;

  public DirectoryMessage(String filePath) {
    this.filePath = filePath;
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }

  @Override
  protected String getContent() {
    File[] files = new File(filePath).listFiles();

    for (File file : files) {
      content.append(file.getName()).append("</br>");
    }

    return content.toString();
  }
}
