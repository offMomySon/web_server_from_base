package filter.path;

public interface AbstractPathChecker {
  void messageSend(String requestPath, String clientIpAddress);

  public boolean isMatchedPath(String requestPath, String ipAddress);
}
