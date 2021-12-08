package path.creater;

import java.util.List;
import path.RequestTargetChecker;
import path.composite.CompositeRequestTargetChecker;
import path.DirectoryRequestTarget;
import path.FileRequestTarget;
import path.RestrictedFileExtension;
import path.SpecificUserRestrictedFileExtension;

public class OrderedRequestTargetChecker {
  private final List<RequestTargetChecker> pathCheckers;

  public OrderedRequestTargetChecker(String clientIpAddress) {
    pathCheckers = List.of(
        new DirectoryRequestTarget(),
        new SpecificUserRestrictedFileExtension(clientIpAddress),
        new RestrictedFileExtension(),
        new FileRequestTarget());
  }

  public RequestTargetChecker create() {
    return new CompositeRequestTargetChecker(pathCheckers);
  }
}
