package sender.factory;


import sender.strategy.MessageResponser;

public interface AbstractMessageResponserFactory {

  public boolean isSupported(String path);

  public MessageResponser createMessageResponser(String path);

}

