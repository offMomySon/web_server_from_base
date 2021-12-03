package thread;

import config.server.thread.ThreadConfig;
import lombok.extern.slf4j.Slf4j;

// 이 클래스는 Thread 를 관리하는 class 이다.
// 그렇기 때문에, 메세지를 보내는 것과 관련된 역할을 하는것은 맞지않다.
// Thread 관리 역할, 메세지 전달(process) 역할을 분리하자.
@Slf4j
public class ThreadCountManager implements ThreadStatus {
  private final ThreadWorker usableThreadWorker;
  private final ThreadWorker waitableThreadWorker;

  public ThreadCountManager(ThreadConfig threadConfig) {
    this.usableThreadWorker = new ThreadWorker(threadConfig.getUsableThreadCount());
    this.waitableThreadWorker = new ThreadWorker(threadConfig.getWaitableThreadCount());
  }

  public boolean isProcessable() {
    return usableThreadWorker.isLeft() || waitableThreadWorker.isLeft();
  }

  public void waitIfNotExistLeftThread() {
    // 접근제어자를 public 으로 주고 client 에서 검사할 수 있게 주는게 좋은가?
    // 가정.
    // 1. public 으로 client 에서 사용할 수 있게 구현.
    //    client 에서 doRequest 사용전에 waitIfNotExistThread() 를 사용하지 않아 버그 발생하지 않나?.
    // 2. private 로 doRequest(지금 메서드) 안에서 동작하도록 구현.
    //    client 의 예상동작과 다를수 있다.
    //    client 가 doRequest 를 호출했는데 바로 반환값이 안돌아 올수 있다.

    //방안
    // 1. document 로 설명을 적어줘야할듯.
    // ex) iterator  hasNext()?
    // 2. 구조체로 감싸서 실행.

    if (usableThreadWorker.isLeft()) {
      return;
    }

    // 코드 리더빌리티( 조건 검사, 실제 wait 분리 )를 위해 public 안에 실제 wait 를 하는 private 메서드를 추가하고 싶다.
    // 1. 그러면 private 메서드의 이름은 어떻게 작성해야 하는가?
    //    그런데, 내부 동작 또한 동일하게 usingCounter.isLeft() 가 true 될 때 까지 기다리는 동작을 한다.
    //    1.1. 이때도 메서드 명을 서술적으로 적어야하는가?
    // 2. 코드 리더빌리티를 위해 private 를 메서드를 작성하는 것이 타당한가?

    // client 의 입장에서는 상관 없을거 같은데?
    waitThread();
  }

  private void waitThread() {
    waitableThreadWorker.increase();

    while (true) {
      if (usableThreadWorker.isLeft()) {
        break;
      }
      try {
        // 1. 사용사능한 thread 가 없다면 wait 하는것이 좋은 코드인가?
        // 2. wait 을 실행하면 다른 thread(request) 로 제어권이 넘어 갈까?
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    waitableThreadWorker.decrease();
  }

  public void runWithOccupiedWorkerThread(Runnable request) {
    usableThreadWorker.increase();

    request.run();

    usableThreadWorker.decrease();
  }

  @Override
  public boolean isAvailable() {
    return isProcessable();
  }
}
