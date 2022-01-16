package action;

import domain.ResourcePath;
import lombok.NonNull;
import response.messageFactory.AbstractMessageFactory;
import response.pretask.PreTask;
import thread.ThreadTask;
import thread.ThreadTaskType;
import util.TriFunction;

import java.io.IOException;
import java.net.Socket;

public class TaskCreator {
    private final AbstractMessageFactory messageFactory;
    private final Socket socket;
    private final ResourcePath resourcePath;
    private final String hostAddress;
    private final ThreadTaskType taskType;
    private final ActionCreator actionCreator;

    private TaskCreator(@NonNull AbstractMessageFactory messageFactory,
                        @NonNull Socket socket,
                        @NonNull ResourcePath resourcePath,
                        @NonNull String hostAddress,
                        @NonNull ThreadTaskType taskType,
                        @NonNull ActionCreator actionCreator) {
        this.messageFactory = messageFactory;
        this.socket = socket;
        this.resourcePath = resourcePath;
        this.hostAddress = hostAddress;
        this.taskType = taskType;
        this.actionCreator = actionCreator;
    }

    public static TaskCreator create(Socket socket,
                                     String hostAddress,
                                     PreTask preTask,
                                     ResourcePath resourcePath,
                                     AbstractMessageFactory mainThreadMessageFactory,
                                     AbstractMessageFactory workerThreadMessageFactory,
                                     TriFunction<PreTask, AbstractMessageFactory, Socket, ActionCreator> actionCreator) throws IOException {

        ThreadTaskType taskType = mainThreadMessageFactory.isSupported(resourcePath) ? ThreadTaskType.MAIN : ThreadTaskType.THREAD;
        AbstractMessageFactory targetMessageFactory = taskType.isMain() ? mainThreadMessageFactory : workerThreadMessageFactory;

        ActionCreator actor = actionCreator.apply(preTask, targetMessageFactory, socket);


        return new TaskCreator(targetMessageFactory, socket, resourcePath, hostAddress, taskType, actor);
    }

    public ThreadTask create() {
        Runnable runnable = actionCreator.get(resourcePath);

        return new ThreadTask(taskType, runnable);
    }

}
