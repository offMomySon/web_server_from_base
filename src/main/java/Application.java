import config.ConfigManager;
import java.io.FileNotFoundException;

public class Application {

  public static void main(String[] args) throws FileNotFoundException {
    Server server = new Server(new ConfigManager());
    server.start();
  }
}
