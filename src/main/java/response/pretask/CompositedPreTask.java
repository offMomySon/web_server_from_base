package response.pretask;

import domain.ResourceMessageCreator;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class CompositedPreTask implements PreTask {
    private final List<PreTask> preTasks;

    public CompositedPreTask(@NonNull List<PreTask> preTasks) {
        this.preTasks = Collections.unmodifiableList(preTasks);
    }

    @Override
    public void doWork(ResourceMessageCreator resourceMessageCreator) {
        preTasks.stream()
                .filter(preTask -> preTask.isWorkablePreTaskRequest(resourceMessageCreator))
                .findFirst()
                .ifPresent(preTask -> preTask.doWork(resourceMessageCreator));
    }

    @Override
    public boolean isWorkablePreTaskRequest(ResourceMessageCreator resourceMessageCreator) {
        return true;
    }
}
