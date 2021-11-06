import controller.ResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.MessageReader;
import sender.MessageSender;
import status.ResourceStatus;

import java.io.*;
import java.lang.invoke.MethodHandles;

public class Servlet implements Runnable{
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ResourceController resourceController;
    private final MessageReader messageReader;
    private final MessageSender messageSender;

    public Servlet(InputStream inputStream, OutputStream outputStream, ResourceController resourceController) {
        this.resourceController = new ResourceController();
        this.messageReader = new MessageReader(inputStream);
        this.messageSender = new MessageSender(outputStream);
    }

    @Override
    public void run() {
        try {
            String httpRequest = messageReader.getHttpRequestHeader();
            String filePath = messageReader.getFilePath(httpRequest);

            ResourceStatus resourceStatus = resourceController.getResourceStatus(filePath);

            messageSender.sendResponse(filePath,resourceStatus);
        } catch (IOException e) {
            logger.error("Exception happen", e);
            e.printStackTrace();
        }
    }
}