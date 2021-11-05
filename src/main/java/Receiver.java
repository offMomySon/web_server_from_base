import controller.ResourceController;
import sender.MessageSender;
import status.ResourceStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Receiver implements Runnable{
    private static final String CR = "\r";
    private static final String LF = "\n";
    private static final String FINISH_READ_REQUEST_MESSAGE = "\r\n\r\n";
    private static final String RESOURCE_ROOT_PATH = "src/main/resources";

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ResourceController resourceController;
    private final MessageSender messageSender;

    public Receiver(InputStream inputStream, OutputStream outputStream, ResourceController resourceController) {
        this.inputStream = new BufferedInputStream(inputStream);
        this.outputStream = new BufferedOutputStream(outputStream);
        this.resourceController = new ResourceController();
        this.messageSender = new MessageSender(outputStream);
    }

    @Override
    public void run() {
        try {
            String httpRequest = getHttpRequest();
            String filePath = RESOURCE_ROOT_PATH + getPath(httpRequest);

            ResourceStatus resourceStatus = resourceController.getResourceStatus(filePath);

            messageSender.sendResponse(filePath,resourceStatus);
        } catch (IOException e) {
            System.out.println("some");
            e.printStackTrace();
        }
    }

    private String getHttpRequest() throws IOException {
        byte[] buffer = new byte[8192];
        int readNo = -1;
        StringBuilder httpRequestMessage = new StringBuilder();

        while((readNo = inputStream.read(buffer)) != -1 ) {
            httpRequestMessage.append(new String(buffer, 0, readNo, StandardCharsets.UTF_8));
            System.out.println("read result\n");
            System.out.println(httpRequestMessage.toString());

            if(httpRequestMessage.toString().contains(FINISH_READ_REQUEST_MESSAGE)){
                System.out.println("finish read http request message.");
                break;
            }
        }

        return httpRequestMessage.toString();
    }

    private String getPath(String httpRequestMessage){
        String startLine = httpRequestMessage.split(CR + LF, 2)[0];
        System.out.println(startLine);
        String path = startLine.split(" ")[1];
        System.out.println(path);

        return path;
    }
}
