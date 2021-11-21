package sender.strategy;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryMessageResponser extends MessageResponser {

  private static final Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final String filePath;

  public DirectoryMessageResponser(String filePath) {
    this.filePath = filePath;
  }

  @Override
  protected String createContent() {
    StringBuilder stringBuilder = new StringBuilder();

    for(File file  : new File(filePath).listFiles()){
      stringBuilder.append(file.getName()).append("</br>");
    }

    return stringBuilder.toString();
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }
}
