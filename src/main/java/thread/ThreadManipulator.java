package thread;

import config.ConfigManager;
import lombok.extern.slf4j.Slf4j;

// 이 클래스는 Thread 를 관리하는 class 이다.
// 그렇기 때문에, 메세지를 보내는 것과 관련된 역할을 하는것은 맞지않다.
// Thread 관리 역할, 메세지 전달(process) 역할을 분리하자.

// ThreadWorker 의 runWithOccupied 구현후
// concrete 한 runnable 제공하는 책임/역할 로 변경됨.. 이걸로 충분한가?
@Slf4j
public class ThreadManipulator {
  // waitIfNotExistLeftThread() ->  runWithOccupiedWorkerThread() 순서 보장을 위해 ,
  // ConfigManager 를 전역으로 가져올 수 있게 했다.

  // config 부분은 전역으로 해도 괜찮지 않을까? 이러면 사실 ThreadConfig 도 전역으로 하면 될거 같은데.
  // 하지만, Config 를 모아둔다는 측면에서 ConfigManager 유지하는 것도 괜찮아 보임.

  // 전역으로 설정하면서 단점은 없을까?
  private static final ThreadWorker usableWorker = new ThreadWorker(ConfigManager.getInstance().getThreadConfig().getUsableThreadCount());
  private static final ThreadWorker waitableWorker = new ThreadWorker(ConfigManager.getInstance().getThreadConfig().getWaitableThreadCount());

  // waitIfNotExistLeftThread() ->  runWithOccupiedWorkerThread() 순서 보장을 위해 beforeWait 를 선언함.
  // 너무 데이터 적인 측면인가?
  private boolean beforeWaited;

  public ThreadManipulator() {
    beforeWaited = false;
  }

  private boolean isProcessable() {
    return usableWorker.isLeft() || waitableWorker.isLeft();
  }

  private void waitIfNotExistLeftThread() {
    // 보장되어야하는 호출순서는 아래와 같다.
    // waitIfNotExistLeftThread() ->  runWithOccupiedWorkerThread()

    // 접근제어자를 public 으로 주고 client 에서 위의 호출 순서를 지키는 방법은 2가지?
    // 가정.
    // 1. public 으로 client 에서 사용할 수 있게 구현.
    //    client 에서 doRequest 사용전에 waitIfNotExistThread() 를 사용하지 않아 버그 발생하지 않나?.
    // 2. private 로 doRequest(지금 메서드) 안에서 동작하도록 구현.
    //    client 의 예상동작과 다를수 있다.
    //    client 가 doRequest 를 호출했는데 바로 반환값이 안돌아 올수 있다.

    //방안 3가지 고려
    // 1. document 로 설명을 적어줘야할듯. -> 강제성이 없음
    // ex) iterator  hasNext(), next()?
    // 2. class 또는 구조체로 감싸서 실행. -> class 의 역할이 빈약. -> ThreadResourceManager 의 메서드에서 처리하기로 생각함. 굳이
    // client 에 보여줄 필요가 있을까?
    // 3. 변수(beforeWaited) 를 두고 waitIfNotExistLeftThread 를 먼저 실행했는지 검사. -> 데이터 중심의 사고라 안 좋은거 같은데

    waitableWorker.runWithOccupied(() -> {
      while (!usableWorker.isLeft()) {
        try {
          // 1. 사용사능한 thread 가 없다면 wait 하는것이 좋은 코드인가?
          // lock 은 풀린다.
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    beforeWaited = true;
  }

  private void runWithOccupiedWorkerThread(Runnable request) {
    if (!beforeWaited) {
      throw new RuntimeException("waitIfNotExistLeftThread 를 먼저 수행해야 합니다.");
    }
    usableWorker.runWithOccupied(request);
  }

  public void run(Runnable processRequest) {
    waitIfNotExistLeftThread();

    runWithOccupiedWorkerThread(processRequest);
  }

  public ThreadStatusSnapShot createStatusSnapShot() {
    boolean processable = isProcessable();
    return () -> processable;
  }
}
