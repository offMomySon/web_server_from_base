package path;

import response.sender.RequestSender;

public interface RequestTargetChecker {
  public RequestSender messageSend(String requestPath);

  public boolean isMatchedPath(String requestPath);
}
