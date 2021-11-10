package config.server.thread;

import lombok.Getter;

@Getter
public class ThreadConfig {

  private int usableThreadCount;
  private int waitableThreadCount;
}
