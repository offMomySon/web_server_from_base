package response.pretask;

import domain.ResourcePath;

public interface PreTask {

    void doWork(ResourcePath resourcePath);

    boolean isWorkablePreTaskRequest(ResourcePath resourcePath);
}
