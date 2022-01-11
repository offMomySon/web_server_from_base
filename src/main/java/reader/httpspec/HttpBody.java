package reader.httpspec;

import java.lang.invoke.MethodHandles;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class HttpBody {
    private final String body;

    public HttpBody(String body) {
        this.body = body;
    }

    public static HttpBody create(BufferedReader reader, int contentLength) {
        char[] charBuffer = new char[8192];
        StringBuilder bodyStringBuilder = new StringBuilder();

        try {
            int curContentLength = 0;
            int bytesRead = -1;

            log.info("Ready to get http body ");
            while ((bytesRead = reader.read(charBuffer)) > 0) {

                bodyStringBuilder.append(charBuffer, 0, bytesRead);

                curContentLength += bytesRead;
                log.debug("Current content length : " + curContentLength);

                if (contentLength == bytesRead) {
                    break;
                }
            }

            log.info("End http body read \n == Result body == \n" + bodyStringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new HttpBody(bodyStringBuilder.toString());
    }

    public String getBody() {
        return body;
    }
}
