package reader.httpspec.startLine;

import domain.ResourcePath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

@Getter
@Slf4j
public class HttpStartLine {
    public static final ResourcePath EMPTY_REQUEST_TARGET = ResourcePath.create("");

    private final HttpMethod httpMethod;
    private final ResourcePath resourcePath;
    private final String httpVersion;

    public HttpStartLine(BufferedReader reader) {
        try {
            log.info("Read get startLine");
            String startLine = reader.readLine();
            log.info("StartLine : " + startLine);

            String[] splitedStartLine = startLine.split(" ");
            httpMethod = HttpMethod.valueOf(splitedStartLine[0].trim());
            resourcePath = ResourcePath.create(splitedStartLine[1].trim());
            httpVersion = splitedStartLine[2].trim();

            log.debug("HttpMethod : " + httpMethod);
            log.debug("RequestTarget : " + resourcePath);
            log.debug("HttpVersion : " + httpVersion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
