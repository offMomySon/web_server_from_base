package Sequence;

import domain.ResourcePath;
import response.pretask.PreTask;

public class PreTaskRunner extends AbstractPreTaskRunner {

    public PreTaskRunner(PreTask preTask) {
        super(preTask);
    }

    @Override
    public void doRun(PreTask preTask, ResourcePath resourcePath) {
        preTask.doWork(resourcePath);
    }

    @Override
    public boolean isPossibleRun(PreTask preTask, ResourcePath resourcePath) {
        return preTask.isWorkablePreTaskRequest(resourcePath);
    }
}
