package response.message.content;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

@Slf4j
public class DirectoryMessage extends Message {
  private final String filePath;

  // Todo]
  //Domain 객체가 RequestTarget 으로 변했는데 어떻게 처리해야하지.
  // 이것도 변경해야 할 거 같은데.
  // 일단 String 으로..
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
