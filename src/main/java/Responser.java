import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Responser implements Runnable{
    private static final String CR = "\r";
    private static final String LF = "\n";
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
            String filePath = readRequestMessage();
            sendTempResponseMsg(filePath);
//            sendResponseMessage();
        } catch (IOException e) {
            System.out.println("some");
            e.printStackTrace();
        }
    }

    private void sendTempResponseMsg(String path) throws IOException {
        File file = new File("src/main/resources" + path);

        if(file.exists()){
            System.out.println("file exist");
            if(file.isDirectory()){
                System.out.println("this is file");

            }else if(file.isFile()){
                System.out.println("this is file.");

                try {
                    System.out.println("star make header");

                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("HTTP/1.1 200 OK \r\n");
                    responseBuilder.append("Content-Length : "+ file.length() +"\r\n");
                    responseBuilder.append("Content-Type: " + getContentType(path) + "\r\n");
                    responseBuilder.append("Date: " + new Date() + "\r\n");
                    responseBuilder.append("Server: jihun server 1.0 \r\n");
                    responseBuilder.append("\r\n");

                    outputStream.write(responseBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    System.out.println("end make header");

                    readFile(new FileInputStream(file), outputStream );

//                    outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    System.out.println("flushed..");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            System.out.println("file not exist");
        }
    }

    private void readFile(InputStream fileInputStream, OutputStream outputStream){
        System.out.println("star readFile");

        try {
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

    private String readRequestMessage() throws IOException {
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

        return getPath(httpRequestMessage.toString());
    }

    private String getPath(String httpRequestMessage){
        String startLine = httpRequestMessage.split(CR + LF, 2)[0];
        System.out.println(startLine);
        String path = startLine.split(" ")[1];
        System.out.println(path);

        return path;
    }
}
