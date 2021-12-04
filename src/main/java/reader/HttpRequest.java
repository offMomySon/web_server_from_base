package reader;

import static java.lang.Integer.parseInt;
import static util.IoUtil.createReader;

import httpspec.HttpMethod;
import java.io.BufferedReader;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String READ_CONTENT_LENGTH = "Content-Length";
  private static final HttpBody HTTP_BODY_NOT_EXIST = null;

  private final HttpStartLine httpStartLine;
  private final HttpHeader httpHeader;
  private final HttpBody httpBody;

  private final BufferedReader reader;

  public HttpRequest(InputStream inputStream) {
    // http read 할떄 BufferedReader 을 httpStartLine,httpHeader, httpBody 에서 따로 만들면 읽지못해서 여기서 BufferedReader 를 생성함.
    reader = createReader(inputStream);

    httpStartLine = new HttpStartLine(reader);
    httpHeader = new HttpHeader(reader);
    if (httpHeader.containKey(READ_CONTENT_LENGTH)) {
      httpBody = new HttpBody(reader, parseInt(httpHeader.getValue(READ_CONTENT_LENGTH)));
    } else {
      httpBody = HTTP_BODY_NOT_EXIST;
    }

    logger.info("Finish to create HttpRequest");
  }

  public boolean isExistHttpBody() {
    return httpBody != HTTP_BODY_NOT_EXIST;
  }

  public HttpMethod getHttpMethod() {
    return httpStartLine.getHttpMethod();
  }

  public String getBody() {
    return httpBody.getBody();
  }

  public String getRequestTarget() {
    return httpStartLine.getRequestTarget();
  }

  public String getRequestVersion() {
    return httpStartLine.getHttpVersion();
  }

  public String getHeaderValue(String key) {
    return httpHeader.getValue(key);
  }

  public Set<String> headerKeySet() {
    return httpHeader.keySet();
  }

  public boolean headerContainKey(String key) {
    return httpHeader.containKey(key);
  }

  public String getValue(String key) {
    return httpHeader.getValue(key);
  }
}
