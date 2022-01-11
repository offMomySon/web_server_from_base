package Sequence;

import domain.ResourcePath;
import lombok.NonNull;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;
import thread.ThreadTask;
import thread.ThreadTaskType;
import thread.ThreadTasker;

import java.util.function.Function;

public class MainTaskRunner extends AbstractMainTaskRunner {
    public MainTaskRunner(@NonNull ThreadTasker threadTasker,
                          @NonNull AbstractMessageFactory mainThreadFactory,
                          @NonNull AbstractMessageFactory workerThreadFactory,
                          @NonNull Function<AbstractMessageFactory, Runnable> runnableCreator) {
        super(threadTasker, mainThreadFactory, workerThreadFactory, runnableCreator);
    }

    @Override
    public void doRun(ResourcePath resourcePath,
                      ThreadTasker threadTasker,
                      AbstractMessageFactory mainThreadFactory,
                      AbstractMessageFactory workerThreadFactory,
                      Function<AbstractMessageFactory, Runnable> messageSender) {

        ThreadTaskType taskType = mainThreadFactory.isSupported(resourcePath) ? ThreadTaskType.MAIN : ThreadTaskType.THREAD;
        AbstractMessageFactory targetFactory = taskType.isMain() ? mainThreadFactory : workerThreadFactory;

        threadTasker.run(new ThreadTask(taskType, messageSender.apply(targetFactory)));
    }
}
