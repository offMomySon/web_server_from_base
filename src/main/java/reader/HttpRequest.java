package reader;

import httpspec.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Set;
import util.IoUtil;

public class HttpRequest {

  HttpStartLine httpStartLine;
  HttpHeader httpHeader;
  HttpBody httpBody;

  public HttpRequest(InputStream inputStream) {
    BufferedReader reader = IoUtil.createReader(inputStream);

    httpStartLine = new HttpStartLine(reader);
    httpHeader = new HttpHeader(reader);
    httpBody = new HttpBody(reader);
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
