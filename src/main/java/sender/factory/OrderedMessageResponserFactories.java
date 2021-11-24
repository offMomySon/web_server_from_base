package sender.factory;

import config.ConfigManager;
import java.util.List;
import sender.factory.thread.ThreadStatus;

//1급 컬랙션
public class OrderedMessageResponserFactories {
  private final List<AbstractMessageResponserFactory> values;

  public OrderedMessageResponserFactories(ConfigManager configmanager, ThreadStatus threadStatus) {
    // 순서를 보장해야합니다.
    this.values = List
        .of(new ServerStopMessageResponserFactory(), new ThreadMessageResponserFactory(threadStatus), new ConfigManagerMessageResponserFactory(configmanager), new PathMessageResponserFactory());
  }

  public AbstractMessageResponserFactory create() {
    return new CompositeMessageResponserFactory(this.values);
  }
}
