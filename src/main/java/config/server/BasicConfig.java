package config.server;

import lombok.Getter;

@Getter
public class BasicConfig {

  private final int port;
  private final String welcomPagePath;

  private static int validatePortNumber(int port){
    if( port < 300 ){
      throw new IllegalArgumentException("포트는 300 번 이상이어야 합니다.");
    }

    return port;
  }
  private static String validateWelcomePagePath(String welcomePagePath){
    return welcomePagePath;
  }

  public BasicConfig(int port, String welcomPagePath) {

    this.port = validatePortNumber(port);
    this.welcomPagePath = validateWelcomePagePath(welcomPagePath);
  }

  public boolean isWelcomePageUri(String uri){

    return welcomPagePath.equals(uri);
  }
}
