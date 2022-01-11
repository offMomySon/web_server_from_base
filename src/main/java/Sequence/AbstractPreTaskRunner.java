package Sequence;

import domain.ResourcePath;
import lombok.NonNull;
import response.pretask.PreTask;

public abstract class AbstractPreTaskRunner implements TaskRunner {
    private final PreTask preTask;

    protected AbstractPreTaskRunner(@NonNull PreTask preTask) {
        this.preTask = preTask;
    }

    public abstract void doRun(PreTask preTask, ResourcePath resourcePath);

    public abstract boolean isPossibleRun(PreTask preTask, ResourcePath resourcePath);

    @Override
    public void doRun(ResourcePath resourcePath) {
        doRun(preTask, resourcePath);
    }

    @Override
    public boolean isPossibleRun(ResourcePath resourcePath) {
        return isPossibleRun(preTask, resourcePath);
    }
}
