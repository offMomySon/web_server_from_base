package response.pretask;

import domain.ResourceMessageCreator;
import lombok.NonNull;

public class FileRequestPreTask implements PreTask {
    private final Runnable task;

    public FileRequestPreTask(@NonNull Runnable task) {
        this.task = task;
    }

    @Override
    public boolean isWorkablePreTaskRequest(ResourceMessageCreator resourceMessageCreator) {
        return true;
    }

    @Override
    public void doWork(ResourceMessageCreator resourceMessageCreator) {
        task.run();
    }
}
