package thread;

import lombok.Getter;

@Getter
public class ThreadTask {
    private ThreadTaskType type;
    private Runnable runnable;

    public ThreadTask(ThreadTaskType type, Runnable runnable) {
        this.type = type;
        this.runnable = runnable;
    }

    public boolean isMainThread() {
        return ThreadTaskType.MAIN == type;
    }

    public void run() {
        runnable.run();
    }
}
