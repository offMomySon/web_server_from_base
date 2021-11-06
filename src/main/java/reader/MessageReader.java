package reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

public class MessageReader {
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final String FINISH_READ_REQUEST_MESSAGE = "\r\n\r\n";
    private static final String RESOURCE_ROOT_PATH = "src/main/resources/files";
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InputStream inputStream;
    private final byte[] buffer;

    public MessageReader(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
        buffer = new byte[8192];
    }

    public String getHttpRequestHeader() throws IOException {
        logger.info("Start to read Http request header.");

        int readNo = -1;
        StringBuilder httpRequestMessage = new StringBuilder();

        while((readNo = inputStream.read(buffer)) != -1 ) {
            logger.info("Reading http request message...");

            httpRequestMessage.append(new String(buffer, 0, readNo, StandardCharsets.UTF_8));

            if(isEndOfHeader(httpRequestMessage)) {
                logger.info("Finish read http request message.");
                break;
            }
        }
        return httpRequestMessage.toString();
    }

    private boolean isEndOfHeader(StringBuilder httpRequestMessage){
        logger.debug("Start checking for end of header");

        int msgLength = httpRequestMessage.length();

        return msgLength>= 4 &&(httpRequestMessage.charAt(msgLength-4) == CR &&
                httpRequestMessage.charAt(msgLength-3) == LF &&
                httpRequestMessage.charAt(msgLength-2) == CR &&
                httpRequestMessage.charAt(msgLength-1) == LF);
    }

    public String getFilePath(String httpRequestMessage){
        logger.debug("Try to extract url path from http request header.");

        String startLine = httpRequestMessage.split(String.valueOf(CR + LF), 2)[0];
        logger.debug("Http Start line : {}", startLine);

        String path = startLine.split(" ")[1];
        logger.debug("URL PATH : {}", path);

        return RESOURCE_ROOT_PATH + path;
    }
}
