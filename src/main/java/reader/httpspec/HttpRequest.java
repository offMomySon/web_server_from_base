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
        reader = createReader(inputStream);

        httpStartLine = new HttpStartLine(reader);
        httpHeader = new HttpHeader(reader);
        if (httpHeader.containKey(READ_CONTENT_LENGTH)) {
            httpBody = HttpBody.create(reader, parseInt(httpHeader.getValue(READ_CONTENT_LENGTH)));
        } else {
            httpBody = HTTP_BODY_NOT_EXIST;
        }

        log.info("Finish to create HttpRequest");
    }
}
