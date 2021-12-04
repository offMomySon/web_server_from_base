package path.creater;

import java.util.List;
import path.AbstractRequestTargetChecker;
import path.composite.CompositeRequestTargetChecker;
import path.DirectoryRequestTarget;
import path.FileRequestTarget;
import path.RestrictedFileExtension;
import path.SpecificUserRestrictedFileExtension;

public class OrderedRequestTargetChecker {
  private final List<AbstractRequestTargetChecker> pathCheckers;

  public OrderedRequestTargetChecker(String clientIpAddress) {
    pathCheckers = List.of(
        new DirectoryRequestTarget(),
        new SpecificUserRestrictedFileExtension(clientIpAddress),
        new RestrictedFileExtension(),
        new FileRequestTarget());
  }

  public AbstractRequestTargetChecker create() {
    return new CompositeRequestTargetChecker(pathCheckers);
  }
}
