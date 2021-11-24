package sender.strategy;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotAvailableServerResponser extends MessageResponser {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected String createContent() {
    return "<h1> server is not available </h1>";
  }

  @Override
  protected String getContentType() {
    return "text/html";
  }
}
