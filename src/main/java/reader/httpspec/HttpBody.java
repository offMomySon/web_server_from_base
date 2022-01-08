package reader.httpspec;

import java.lang.invoke.MethodHandles;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class HttpBody {

    private final StringBuilder bodyStringBuilder = new StringBuilder();
    private final String body;
    private final char[] charBuffer = new char[8192];

    // todo
    // 이런 애들은 생성자 만들기 어려운데..?
    public HttpBody(BufferedReader reader, int contentLength) {
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

            body = bodyStringBuilder.toString();
            log.info("End http body read \n == Result body == \n" + body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBody() {
        return bodyStringBuilder.toString();
    }
}
