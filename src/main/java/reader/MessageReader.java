package reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MessageReader {
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final String FINISH_READ_REQUEST_MESSAGE = "\r\n\r\n";
    private static final String RESOURCE_ROOT_PATH = "src/main/resources";

    private final InputStream inputStream;
    private final byte[] buffer;

    public MessageReader(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
        buffer = new byte[8192];
    }

    public String getHttpRequestHeader() throws IOException {
        int readNo = -1;
        StringBuilder httpRequestMessage = new StringBuilder();

        while((readNo = inputStream.read(buffer)) != -1 ) {
            httpRequestMessage.append(new String(buffer, 0, readNo, StandardCharsets.UTF_8));
            System.out.println("read result\n");

            if(isEndOfHeader(httpRequestMessage)) {
                System.out.println("finish read http request message.");
                break;
            }
        }

        return httpRequestMessage.toString();
    }

    private boolean isEndOfHeader(StringBuilder httpRequestMessage){
        int msgLength = httpRequestMessage.length();

        return msgLength>= 4 &&(httpRequestMessage.charAt(msgLength-4) == CR &&
                httpRequestMessage.charAt(msgLength-3) == LF &&
                httpRequestMessage.charAt(msgLength-2) == CR &&
                httpRequestMessage.charAt(msgLength-1) == LF);
    }

    public String getFilePath(String httpRequestMessage){
        String startLine = httpRequestMessage.split(String.valueOf(CR + LF), 2)[0];
        System.out.println(startLine);
        String path = startLine.split(" ")[1];
        System.out.println(path);

        return RESOURCE_ROOT_PATH + path;
    }
}
