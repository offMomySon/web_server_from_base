package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

// 1. value 를 한번 감싸서 동작을 캡슐화하는건 쉬움.
// 2. Runnable 구문을 ThreadWorker 에 포함시키는걸 생각하기 어렵다.
// currentValue 증가 감소를 외부에 노출하지 않기 위해? 왜 해야하지? ThreadResourceManager 에서 Runnable 을 실행하면 안되나?
// ThreadResourceManager 는 결국 Runnable 의 내용을 concrete 하게 제공하기 위한 객체일 뿐인가.

// ThreadWorker 의 책임은 외부에서 받은 Runnable 의 실행,
// 그리고 실행하기 위한 currentValue 제어.

@Slf4j
public class ThreadWorker {
    private final int maxValue;
    private AtomicInteger currentValue = new AtomicInteger();

    public ThreadWorker(int usableThread) {
        this.maxValue = validatePositive(usableThread);
    }

    private int validatePositive(int usableThread) {
        if (usableThread < 0) {
            throw new IllegalArgumentException("maxValue 는 0 미만일 수 없습니다.");
        }
        return usableThread;
    }

    public boolean isLeft() {
        log.info("max = {}, current = {}", maxValue, currentValue);
        return currentValue.get() < maxValue;
    }

    public void runWithOccupied(Runnable runnable) {
        currentValue.decrementAndGet();
        runnable.run();
        currentValue.incrementAndGet();
    }
}
