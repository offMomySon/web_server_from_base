package filter.path;

import java.util.List;

public class OrderedPathChecker {
  List<AbstractPathChecker> pathCheckers;

  public OrderedPathChecker(String clientIpAddress) {
    pathCheckers = List.of(
        new DirectoryPath(),
        new SpecificUserRestrictedFileExtension(clientIpAddress),
        new RestrictedFileExtension(),
        new FilePath());
  }

  public AbstractPathChecker create() {
    return new CompositePathChecker(pathCheckers);
  }
}
