package reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeader {

  private final Map<String, String> header = new HashMap<>();
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public HttpHeader(BufferedReader reader) {
    try {
      String headerLine = null;
      logger.info("Ready to get http header");
      while ((headerLine = reader.readLine()) != null && (headerLine.length() != 0)) {
        logger.debug(headerLine);

        String[] headers = headerLine.split(":");
        String key = headers[0].trim();
        String value = headers[1].trim();

        if (key.length() == 0) {
          throw new IllegalArgumentException("key is empty value.");
        } else if (value.length() == 0) {
          throw new IllegalArgumentException("value is empty value.");
        }

        header.put(key, value);
      }
      logger.info("Result of http header == \n" + header);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Set<String> keySet() {
    return header.keySet();
  }

  public boolean containKey(String key) {
    return header.containsKey(key);
  }

  public String getValue(String key) {
    return header.get(key);
  }
}
