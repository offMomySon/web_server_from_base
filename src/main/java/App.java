import config.ConfigManager;

public class App {

    public static void main(String[] args) {
        Server server = new Server(ConfigManager.getInstance());
        server.start();
    }
}
