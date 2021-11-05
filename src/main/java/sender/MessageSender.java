package sender;

import status.ResourceStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

public class MessageSender {
    private final static String DEFAULT_CONTENT_TYPE = "text/html";
    private final OutputStream outputStream;

    public MessageSender(OutputStream outputStream) {
        this.outputStream = new BufferedOutputStream(outputStream);
    }

    public void sendResponse(String filePath, ResourceStatus resourceStatus) throws IOException {
       switch (resourceStatus){
           case FILE_EXIST:
               sendExistFileResponse(filePath);
               break;
           case DIRECTORY_EXIST:
               sendDirectoryResponse(filePath);
               break;
           case PATH_NOT_EXIST:
               sendFileNotExistResponse();
       }
    }

    private void sendExistFileResponse(String filePath){
        System.out.println("this is file.");

        try {
            File file = new File(filePath);
            String responseMsg = createHeader(file.length(), getContentType(filePath));

            outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));

            outputFile(filePath, outputStream );

            outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            System.out.println("flushed..");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDirectoryResponse(String filePath){
        System.out.println("curDirFilesBuilder start");

        File file = new File(filePath);

        StringBuilder curDirFilesBuilder = new StringBuilder();
        curDirFilesBuilder.append("this is file/dir list</br>");
        for(File curDirFile : Objects.requireNonNull(file.listFiles())){
            curDirFilesBuilder.append("/").append(curDirFile.getName()).append("</br>");
            System.out.println(curDirFilesBuilder);
        }

        try {
            String responseMsg = createHeader(curDirFilesBuilder.toString().length(), DEFAULT_CONTENT_TYPE);
            responseMsg += curDirFilesBuilder + "\r\n";

            outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFileNotExistResponse(){
        try {
            String notExistErrMsg = "File/Directory not exist";

            String responseMsg = createHeader(notExistErrMsg.length(), DEFAULT_CONTENT_TYPE);
            responseMsg += notExistErrMsg + "\r\n";

            outputStream.write(responseMsg.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void outputFile(String filePath, OutputStream outputStream){
        System.out.println("star readFile");

        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int readNo = -1;

            while((readNo = fileInputStream.read(buffer)) != -1) {
                System.out.println("loop read..");
                outputStream.write(buffer,0,readNo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("end readFile..");
    }

    private String createHeader(long contentLength, String contentType ){
        return "HTTP/1.1 200 OK \r\n" +
                "Content-Length : " + contentLength + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Date: " + new Date() + "\r\n" +
                "Server: jihun server 1.0 \r\n" +
                "\r\n";
    }

    private String getContentType(String path){
        if (path.endsWith(".html") || path.endsWith(".htm"))
            return "text/html";
        else if (path.endsWith(".txt") || path.endsWith(".java"))
            return "text/plain";
        else if (path.endsWith(".gif"))
            return "image/gif";
        else if (path.endsWith(".png"))
            return "image/png";
        else if (path.endsWith(".class"))
            return "application/octet-stream";
        else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        else if (path.endsWith(".mpeg"))
            return "video/mpeg";
        else if (path.endsWith(".ts"))
            return "video/MP2T";
        else if (path.endsWith(".avi"))
            return "video/x-msvideo";
        else
            return "text/plain";
    }
}
