package sender.factory;

import java.util.List;
import sender.strategy.MessageResponser;

public class CompositeMessageResponserFactory implements AbstractMessageResponserFactory{

  private final List<AbstractMessageResponserFactory> abstractMessageResponserFactories;

  //todo - 깊은 복사 , 얕은 복사
  public CompositeMessageResponserFactory(List<AbstractMessageResponserFactory> abstractMessageResponserFactories) {
    this.abstractMessageResponserFactories = abstractMessageResponserFactories;
  }

  @Override
  public MessageResponser createMessageResponser(String path) {
    return abstractMessageResponserFactories.stream()
        .filter(abs->abs.isSupported(path))
        .findFirst()
        .map(abs->abs.createMessageResponser(path))
        .orElseThrow();
  }

  @Override
  public boolean isSupported(String path) {
    return abstractMessageResponserFactories.stream().anyMatch(abs->abs.isSupported(path));
  }
}
