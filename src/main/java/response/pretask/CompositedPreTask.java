package response.pretask;

import domain.ResourcePath;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class CompositedPreTask implements PreTask {
    private final List<PreTask> preTasks;

    public CompositedPreTask(@NonNull List<PreTask> preTasks) {
        this.preTasks = Collections.unmodifiableList(preTasks);
    }

    @Override
    public void doWork(ResourcePath resourcePath) {
        preTasks.stream()
                .filter(preTask -> preTask.isWorkablePreTaskRequest(resourcePath))
                .findFirst()
                .ifPresent(preTask -> preTask.doWork(resourcePath));
    }

    @Override
    public boolean isWorkablePreTaskRequest(ResourcePath resourcePath) {
        return true;
    }
}
