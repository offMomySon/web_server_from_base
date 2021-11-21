package sender.strategy;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoThreadAvailableMessageResponser extends MessageResponser {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected String createContent() {
    return "<h1> There are no threads available.</br> Please try again later </h1>";
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }
}
