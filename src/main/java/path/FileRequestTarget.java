package path;

import response.sender.RequestSender;
import response.sender.ThreadRelatedRequestSender;

public class FileRequestTarget implements RequestTargetChecker {
  @Override
  public RequestSender messageSend(String requestPath) {
    return new ThreadRelatedRequestSender();
  }

  @Override
  public boolean isMatchedPath(String requestPath) {
    return true;
  }
}
