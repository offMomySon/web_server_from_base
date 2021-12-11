package reader.httpspec.startLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.HttpRequest;

@Getter
@Slf4j
public class HttpStartLine {
  public static final RequestTarget EMPTY_REQUEST_TARGET = RequestTarget.create("");

  private final HttpMethod httpMethod;
  private final RequestTarget requestTarget;
  private final String httpVersion;

  public HttpStartLine(BufferedReader reader) {
    try {
      log.info("Read get startLine");
      String startLine = reader.readLine();
      log.info("StartLine : " + startLine);

      String[] splitedStartLine = startLine.split(" ");
      httpMethod = HttpMethod.valueOf(splitedStartLine[0].trim());
      requestTarget = RequestTarget.create(splitedStartLine[1].trim());
      httpVersion = splitedStartLine[2].trim();

      log.debug("HttpMethod : " + httpMethod);
      log.debug("RequestTarget : " + requestTarget);
      log.debug("HttpVersion : " + httpVersion);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
