package action;

import domain.ResourcePath;
import lombok.NonNull;
import response.pretask.PreTask;

public class PreTaskActionCreator implements ActionCreator {
    private final PreTask preTask;

    public PreTaskActionCreator(@NonNull PreTask preTask) {
        this.preTask = preTask;
    }

    public Runnable get(@NonNull ResourcePath resourcePath, @NonNull PreTask preTask) {
        return () -> {
            if (preTask.isWorkablePreTaskRequest(resourcePath)) {
                preTask.doWork(resourcePath);
            }
        };
    }

    @Override
    public Runnable get(@NonNull ResourcePath resourcePath) {
        return get(resourcePath, preTask);
    }
}
