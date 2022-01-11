package response.pretask;

import domain.ResourcePath;
import lombok.NonNull;

public class FileRequestPreTask implements PreTask {
    private final Runnable task;

    public FileRequestPreTask(@NonNull Runnable task) {
        this.task = task;
    }

    @Override
    public boolean isWorkablePreTaskRequest(ResourcePath resourcePath) {
        return resourcePath.isFile();
    }

    @Override
    public void doWork(ResourcePath resourcePath) {
        task.run();
    }
}
