import config.ConfigManager;
import java.io.FileNotFoundException;

public class App {

  public static void main(String[] args) throws FileNotFoundException {
    Server server = new Server(ConfigManager.getInstance());
    server.start();
  }
}
