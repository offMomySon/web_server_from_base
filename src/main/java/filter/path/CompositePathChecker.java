package filter.path;

import java.util.List;

public class CompositePathChecker implements AbstractPathChecker {

  private final List<AbstractPathChecker> pathCheckers;

  public CompositePathChecker(List<AbstractPathChecker> paths) {
    this.pathCheckers = paths;
  }

  @Override
  public void messageSend(String requestPath, String clientIpAddress) {
    final AbstractPathChecker abstractPathChecker = pathCheckers
        .stream()
        .filter(pathChekcer -> pathChekcer.isMatchedPath(requestPath, clientIpAddress))
        .findFirst()
        .orElseThrow();

    abstractPathChecker.messageSend(requestPath, clientIpAddress);
  }

  public boolean isMatchedPath(String requestPath, String clientIpAddress) {
    return pathCheckers.stream().anyMatch(pathChecker -> pathChecker.isMatchedPath(requestPath, clientIpAddress));
  }
}
