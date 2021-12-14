package reader.httpspec;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.startLine.HttpStartLine;

import java.io.BufferedReader;
import java.io.InputStream;

import static java.lang.Integer.parseInt;
import static util.IoUtil.createReader;

@Getter
@Slf4j
public class HttpRequest {
    private static final String READ_CONTENT_LENGTH = "Content-Length";
    private static final HttpBody HTTP_BODY_NOT_EXIST = null;

    private final HttpStartLine httpStartLine;
    private final HttpHeader httpHeader;
    private final HttpBody httpBody;

    private final BufferedReader reader;

    public HttpRequest(InputStream inputStream) {
        // Todo
        // http read 할떄 BufferedReader 을 httpStartLine,httpHeader, httpBody 에서 따로 만들면 읽지못해서 여기서 BufferedReader 를 생성함.
        reader = createReader(inputStream);

        httpStartLine = new HttpStartLine(reader);
        httpHeader = new HttpHeader(reader);
        if (httpHeader.containKey(READ_CONTENT_LENGTH)) {
            httpBody = new HttpBody(reader, parseInt(httpHeader.getValue(READ_CONTENT_LENGTH)));
        } else {
            httpBody = HTTP_BODY_NOT_EXIST;
        }

        log.info("Finish to create HttpRequest");
    }
}
