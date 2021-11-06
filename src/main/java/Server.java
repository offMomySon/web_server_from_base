import controller.ResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private static final Socket UNBOUNDED = null;
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public final ResourceController resourceController = createResourceController();
    private final ServerSocket serverSocket = createServerSocket();
    private final ExecutorService executorService = ForkJoinPool.commonPool();

    public ServerSocket createServerSocket() {
        try {
            return new ServerSocket(5001);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceController createResourceController(){
        if(resourceController == null){
            return new ResourceController();
        }
        return null;
    }

    public void start(){
        Socket socket = UNBOUNDED;
        try {
            while(true){
                logger.info("waiting.. request");
                socket = serverSocket.accept();

                logger.info("accept.. request");
                logger.info("New Client Connect! Connected IP : {}, Port : {}}", socket.getInetAddress(), socket.getPort());

                executorService.submit(new Servlet(socket.getInputStream(), socket.getOutputStream(), resourceController));

                socket = UNBOUNDED;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null) socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
