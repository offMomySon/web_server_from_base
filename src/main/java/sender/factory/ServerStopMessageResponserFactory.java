package sender.factory;

import sender.strategy.MessageResponser;
import sender.strategy.NotAvailableServerResponser;

public class ServerStopMessageResponserFactory implements AbstractMessageResponserFactory{

  @Override
  public boolean isSupported(String path) {
    return true;
  }

  @Override
  public MessageResponser createMessageResponser(String path) {
    return new NotAvailableServerResponser();
  }
}
