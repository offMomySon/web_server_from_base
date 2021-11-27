package sender.strategy;

import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirectoryMessageResponser extends MessageResponser {
  private final String filePath;

  public DirectoryMessageResponser(String filePath) {
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
