package config.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Config<T> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final String path;
  private final Class<T> clazz;

  public T create() {
    try {
      return objectMapper.readValue(new File(path), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

