package reader.httpspec;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reader.httpspec.method.HttpMethod;

@Getter
@Slf4j
public class HttpStartLine {
  private final HttpMethod httpMethod;
  private final String requestTarget;
  private final String httpVersion;

  public HttpStartLine(BufferedReader reader) {
    try {
      log.info("Read get startLine");
      String startLine = reader.readLine();
      log.info("StartLine : " + startLine);

      String[] splitedStartLine = startLine.split(" ");
      httpMethod = HttpMethod.valueOf(splitedStartLine[0].trim());
      requestTarget = new PathModifier().modify(splitedStartLine[1].trim());
      httpVersion = splitedStartLine[2].trim();

      log.debug("HttpMethod : " + httpMethod);
      log.debug("RequestTarget : " + requestTarget);
      log.debug("HttpVersion : " + httpVersion);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static class PathModifier {
    private String modify(String requestTarget) {
      requestTarget = requestTarget.replaceAll("//", "/");
      requestTarget = removeRelativePath(requestTarget);

      return requestTarget;
    }

    private String removeRelativePath(String requestTarget) {
      Stack<String> pathStack = new Stack<>();
      String[] splitPath = requestTarget.split("/");

      for (String path : splitPath) {
        if (path.length() == 0) {
          continue;
        }

        if (path.equals("..")) {
          if (pathStack.isEmpty()) {
            throw new RuntimeException("wrong request target.");
          }
          pathStack.pop();
        } else {
          pathStack.push(path);
        }
      }

      return pathStack.stream().collect(Collectors.joining("/", "/", ""));
    }
  }
}
