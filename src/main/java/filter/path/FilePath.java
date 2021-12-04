package filter.path;

public class FilePath implements AbstractPathChecker {
  @Override
  public void messageSend(String requestPath, String ipAddress) {

  }

  @Override
  public boolean isMatchedPath(String requestPath, String ipAddress) {
    return true;
  }
}
