package path;

import response.messageFactory.PathMessageFactory;
import response.sender.NotThreadRelatedRequestSender;
import response.sender.RequestSender;

public class DirectoryRequestTarget implements RequestTargetChecker {
  private final PathMessageFactory pathMessageResponserFactory = new PathMessageFactory();

  @Override
  public RequestSender messageSend(String requestPath) {
    return new NotThreadRelatedRequestSender();
  }

  @Override
  public boolean isMatchedPath(String requestPath) {
    return requestPath.endsWith("/");
  }
}
