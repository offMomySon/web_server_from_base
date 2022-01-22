package action;

import domain.ResourceMessageCreator;
import lombok.NonNull;
import response.pretask.PreTask;

public class PreTaskActionCreator implements ActionCreator {
    private final PreTask preTask;

    public PreTaskActionCreator(@NonNull PreTask preTask) {
        this.preTask = preTask;
    }

    public Runnable get(@NonNull ResourceMessageCreator resourceMessageCreator, @NonNull PreTask preTask) {
        return () -> {
            if (preTask.isWorkablePreTaskRequest(resourceMessageCreator)) {
                preTask.doWork(resourceMessageCreator);
            }
        };
    }

    @Override
    public Runnable get(@NonNull ResourceMessageCreator resourceMessageCreator) {
        return get(resourceMessageCreator, preTask);
    }
}
