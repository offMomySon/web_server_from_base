package Sequence;

import domain.ResourcePath;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class CompositedTaskRunner implements TaskRunner {
    private final List<TaskRunner> taskRunners;

    public CompositedTaskRunner(@NonNull List<TaskRunner> taskRunners) {
        this.taskRunners = Collections.unmodifiableList(taskRunners);
    }

    @Override
    public void doRun(@NonNull ResourcePath resourcePath) {
        taskRunners.stream().forEach(t -> t.doRun(resourcePath));
    }

    @Override
    public boolean isPossibleRun(@NonNull ResourcePath resourcePath) {
        return true;
    }
}
