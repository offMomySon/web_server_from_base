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
//
//    private void sendResponseMsg(String path) throws IOException {
//        File file = new File(path);
//
//        if(file.exists()){
//            System.out.println("file exist");
//            if(file.isDirectory()){
//                System.out.println("this is directory");
//
//                System.out.println("curDirFilesBuilder start");
//                StringBuilder curDirFilesBuilder = new StringBuilder();
//                for(File curDirFile : file.listFiles()){
//                    curDirFilesBuilder.append(curDirFile.getName() + "\r\n");
//                    System.out.println(curDirFilesBuilder);
//                }
//
//                StringBuilder responseBuilder = new StringBuilder();
//                responseBuilder.append("HTTP/1.1 200 OK \r\n");
//                responseBuilder.append("Content-Length : "+ curDirFilesBuilder.toString().length() +"\r\n");
//                responseBuilder.append("Content-Type: text/html;charset=utf-8\r\n");
//                responseBuilder.append("Date: " + new Date() + "\r\n");
//                responseBuilder.append("Server: jihun server 1.0 \r\n");
//                responseBuilder.append("\r\n");
//                responseBuilder.append("");
//                responseBuilder.append(curDirFilesBuilder);
//                responseBuilder.append("\r\n");
//
//                outputStream.write(responseBuilder.toString().getBytes(StandardCharsets.UTF_8));
//                outputStream.flush();
//            }else if(file.isFile()){
//                System.out.println("this is file.");
//
//                try {
//                    System.out.println("star make header");
//
//                    StringBuilder responseBuilder = new StringBuilder();
//                    responseBuilder.append("HTTP/1.1 200 OK \r\n");
//                    responseBuilder.append("Content-Length : "+ file.length() +"\r\n");
//                    responseBuilder.append("Content-Type: " + getContentType(path) + "\r\n");
//                    responseBuilder.append("Date: " + new Date() + "\r\n");
//                    responseBuilder.append("Server: jihun server 1.0 \r\n");
//                    responseBuilder.append("\r\n");
//
//                    outputStream.write(responseBuilder.toString().getBytes(StandardCharsets.UTF_8));
//                    System.out.println("end make header");
//
//                    readFile(new FileInputStream(file), outputStream );
//
//                    outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
//                    outputStream.flush();
//                    System.out.println("flushed..");
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }else{
//            System.out.println("file not exist");
//        }
//    }



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
