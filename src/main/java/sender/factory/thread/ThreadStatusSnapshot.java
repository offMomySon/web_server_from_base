package sender.factory.thread;

@FunctionalInterface
public interface ThreadStatusSnapshot {
  public boolean isAvailable();
}
