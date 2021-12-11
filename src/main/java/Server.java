import config.ConfigManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;
import response.messageFactory.creater.OrderedMessageResponserFactories;

@Slf4j
public class Server {
  private static final Socket UNBOUNDED = null;

  private final ServerSocket serverSocket;

  public Server(ConfigManager configManager) {
    serverSocket = createServerSocket(configManager.getBasicConfig().getPort());
  }

  private static ServerSocket createServerSocket(int port) {
    try {
      return new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void start() {
    Socket socket = UNBOUNDED;
    try {
      while (true) {
        log.info("waiting.. request");
        socket = serverSocket.accept();

        log.info("accept.. request");
        log.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress().getHostAddress(), socket.getPort());

        HttpRequest httpRequest = new HttpRequest(socket.getInputStream());

        httpRequest.getRequestTarget();
//        RequestTargetChecker requestTargetChecker = new OrderedRequestTargetChecker(socket.getInetAddress().getHostAddress()).create();
//        RequestSender requestSender = requestTargetChecker.messageSend(httpRequest.getRequestTarget());
//        requestSender.doProcess(httpRequest, socket.getOutputStream());



        socket = UNBOUNDED;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null) {
          socket.close();
        }
      } catch (IOException ioException) {
        throw new RuntimeException(ioException);
      }
    }
  }


}
