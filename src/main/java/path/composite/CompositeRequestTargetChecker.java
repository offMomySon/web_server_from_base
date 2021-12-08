package path.composite;

import java.util.List;
import path.RequestTargetChecker;
import response.sender.RequestSender;

public class CompositeRequestTargetChecker implements RequestTargetChecker {
  private final List<RequestTargetChecker> pathCheckers;

  public CompositeRequestTargetChecker(List<RequestTargetChecker> paths) {
    this.pathCheckers = paths;
  }

  @Override
  public RequestSender messageSend(String requestPath) {
    RequestSender requestSender = pathCheckers
        .stream()
        .filter(pathChekcer -> pathChekcer.isMatchedPath(requestPath))
        .findFirst()
        .map(pathChekcer -> pathChekcer.messageSend(requestPath))
        .orElseThrow();

    return requestSender;
  }

  public boolean isMatchedPath(String requestPath) {
    return pathCheckers.stream().anyMatch(pathChecker -> pathChecker.isMatchedPath(requestPath));
  }
}
