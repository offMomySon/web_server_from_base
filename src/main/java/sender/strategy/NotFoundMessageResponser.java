package sender.strategy;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotFoundMessageResponser extends MessageResponser {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected String createContent() {
    return "<h1> file not exist </h1>";
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }
}
