package thread;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ThreadTask {
    private ThreadTaskType type;
    private Runnable runnable;

    public ThreadTask(@NonNull ThreadTaskType type, @NonNull Runnable runnable) {
        this.type = type;
        this.runnable = runnable;
    }

    public static ThreadTask empty(ThreadTaskType type) {
        return new ThreadTask(type, () -> {
        });
    }

    public ThreadTask andThen(Runnable task) {
        return new ThreadTask(type, () -> {
            this.run();
            task.run();
        });
    }

    public boolean isMainThread() {
        return ThreadTaskType.MAIN == type;
    }

    public void run() {
        runnable.run();
    }
}
