package reader.httpspec.startLine;

import domain.ResourceMessageCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

@Getter
@Slf4j
public class HttpStartLine {
    public static final ResourceMessageCreator EMPTY_REQUEST_TARGET = ResourceMessageCreator.create("");

    private final HttpMethod httpMethod;
    private final ResourceMessageCreator resourceMessageCreator;
    private final String httpVersion;

    public HttpStartLine(BufferedReader reader) {
        try {
            log.info("Read get startLine");
            String startLine = reader.readLine();
            log.info("StartLine : " + startLine);

            String[] splitedStartLine = startLine.split(" ");
            httpMethod = HttpMethod.valueOf(splitedStartLine[0].trim());
            resourceMessageCreator = ResourceMessageCreator.create(splitedStartLine[1].trim());
            httpVersion = splitedStartLine[2].trim();

            log.debug("HttpMethod : " + httpMethod);
            log.debug("RequestTarget : " + resourceMessageCreator);
            log.debug("HttpVersion : " + httpVersion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
