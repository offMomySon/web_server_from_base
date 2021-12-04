package path.composite;

import java.util.List;
import path.AbstractRequestTargetChecker;
import response.sender.RequestSender;

public class CompositeRequestTargetChecker implements AbstractRequestTargetChecker {
  private final List<AbstractRequestTargetChecker> pathCheckers;

  public CompositeRequestTargetChecker(List<AbstractRequestTargetChecker> paths) {
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
