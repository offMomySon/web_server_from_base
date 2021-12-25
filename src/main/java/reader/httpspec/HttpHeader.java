package reader.httpspec;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpHeader {
  private final Map<String, String> header = new HashMap<>();

  public HttpHeader(BufferedReader reader) {
    try {
      String headerLine = null;
      log.info("Ready to get http header");
      while ((headerLine = reader.readLine()) != null && (headerLine.length() != 0)) {
        log.debug(headerLine);

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
      log.info("Result of http header == \n" + header);
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
