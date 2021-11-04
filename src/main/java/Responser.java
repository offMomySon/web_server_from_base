import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Responser implements Runnable{
    private static final String FINISH_READ_REQUEST_MESSAGE = "\r\n\r\n";

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public Responser(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
        this.outputStream = new BufferedOutputStream(outputStream);
    }

    @Override
    public void run() {
        try {
            getRequestMessage();
            sendResponseMessage();
        } catch (IOException e) {
            System.out.println("some");
            e.printStackTrace();
        }
    }

    private void sendResponseMessage() throws IOException {
        outputStream.write("HTTP/1.1 200 OK \r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("Content-Type: text/html;charset=utf-8\r\n".getBytes(StandardCharsets.UTF_8));
        byte[] body = "Hello World".getBytes(StandardCharsets.UTF_8);
        String lenMessage = "Content-Length: " + body.length + "\r\n";
        outputStream.write(lenMessage.getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));

        outputStream.write(body, 0, body.length);
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    private void getRequestMessage() throws IOException {
        byte[] buffer = new byte[8192];
        int readNo = -1;
        StringBuilder sb = new StringBuilder();

        while((readNo = inputStream.read(buffer)) != -1 ) {
            sb.append(new String(buffer, 0, readNo, StandardCharsets.UTF_8));

            if(sb.toString().contains(FINISH_READ_REQUEST_MESSAGE)){
                System.out.println("finish read http request message.");
                break;
            }
        }
    }
}
