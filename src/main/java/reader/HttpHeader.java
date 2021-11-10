package reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static util.IoUtil.*;

public class HttpHeader {

  private final Map<String, String> header = new HashMap<>();
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public HttpHeader(BufferedReader reader) {
    try {
      String headerLine = null;
      logger.info("Ready to get http header");
      while ((headerLine = reader.readLine()) != null && (headerLine.length() != 0)) {
        logger.debug("Loop.. get http header");
        logger.debug(headerLine);

        String key = headerLine.split(":", 2)[0].trim();
        String value = headerLine.split(":", 2)[1].trim();

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
