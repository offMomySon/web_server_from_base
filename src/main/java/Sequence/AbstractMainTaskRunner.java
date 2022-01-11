package Sequence;

import domain.ResourcePath;
import lombok.NonNull;
import response.messageFactory.AbstractMessageFactory;
import thread.ThreadTask;
import thread.ThreadTasker;

import java.util.function.Function;

public abstract class AbstractMainTaskRunner implements TaskRunner {
    private final ThreadTasker threadTasker;
    private final AbstractMessageFactory mainThreadFactory;
    private final AbstractMessageFactory workerThreadFactory;
    Function<AbstractMessageFactory, Runnable> runnableCreator;

    protected AbstractMainTaskRunner(@NonNull ThreadTasker threadTasker,
                                     @NonNull AbstractMessageFactory mainThreadFactory,
                                     @NonNull AbstractMessageFactory workerThreadFactory,
                                     @NonNull Function<AbstractMessageFactory, Runnable> runnableCreator) {
        this.threadTasker = threadTasker;
        this.mainThreadFactory = mainThreadFactory;
        this.workerThreadFactory = workerThreadFactory;
        this.runnableCreator = runnableCreator;
    }

    public abstract void doRun(ResourcePath resourcePath,
                               ThreadTasker threadTasker,
                               AbstractMessageFactory mainThreadFactory,
                               AbstractMessageFactory workerThreadFactory,
                               Function<AbstractMessageFactory, Runnable> runnableCreator);

    @Override
    public void doRun(ResourcePath resourcePath) {
        doRun(resourcePath, threadTasker, mainThreadFactory, workerThreadFactory, runnableCreator);
    }

    @Override
    public boolean isPossibleRun(ResourcePath resourcePath) {
        return true;
    }
}
