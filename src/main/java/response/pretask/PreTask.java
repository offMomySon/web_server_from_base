package response.pretask;

import domain.ResourceMessageCreator;

public interface PreTask {

    void doWork(ResourceMessageCreator resourceMessageCreator);

    boolean isWorkablePreTaskRequest(ResourceMessageCreator resourceMessageCreator);
}
