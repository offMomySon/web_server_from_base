package sender.factory;

import sender.strategy.MessageResponser;

public interface AbstractMessageResponserFactory {

  MessageResponser createMessageResponser(String requestTarget);

  boolean isSupported(String filePath);
}
