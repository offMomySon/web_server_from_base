package path;

import response.sender.RequestSender;

public interface AbstractRequestTargetChecker {
  public RequestSender messageSend(String requestPath);

  public boolean isMatchedPath(String requestPath);
}
