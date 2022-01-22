package action;

import domain.ResourceMessageCreator;
import lombok.NonNull;
import response.messageFactory.AbstractMessageFactory;
import response.pretask.PreTask;
import thread.ThreadTask;
import thread.ThreadTaskType;
import util.TriFunction;

import java.net.Socket;

public class TaskCreator {
    private final AbstractMessageFactory messageFactory;
    private final Socket socket;
    private final ResourceMessageCreator resourceMessageCreator;
    private final String hostAddress;
    private final ThreadTaskType taskType;
    private final ActionCreator actionCreator;

    private TaskCreator(@NonNull AbstractMessageFactory messageFactory,
                        @NonNull Socket socket,
                        @NonNull ResourceMessageCreator resourceMessageCreator,
                        @NonNull String hostAddress,
                        @NonNull ThreadTaskType taskType,
                        @NonNull ActionCreator actionCreator) {
        this.messageFactory = messageFactory;
        this.socket = socket;
        this.resourceMessageCreator = resourceMessageCreator;
        this.hostAddress = hostAddress;
        this.taskType = taskType;
        this.actionCreator = actionCreator;
    }

    public static TaskCreator create(Socket socket,
                                     String hostAddress,
                                     PreTask preTask,
                                     ResourceMessageCreator resourceMessageCreator,
                                     AbstractMessageFactory mainThreadMessageFactory,
                                     AbstractMessageFactory workerThreadMessageFactory,
                                     TriFunction<PreTask, AbstractMessageFactory, Socket, ActionCreator> actionCreator) {

        ThreadTaskType taskType = mainThreadMessageFactory.isSupported() ? ThreadTaskType.MAIN : ThreadTaskType.THREAD;
        AbstractMessageFactory targetMessageFactory = taskType.isMain() ? mainThreadMessageFactory : workerThreadMessageFactory;

        ActionCreator actor = actionCreator.apply(preTask, targetMessageFactory, socket);


        return new TaskCreator(targetMessageFactory, socket, resourceMessageCreator, hostAddress, taskType, actor);
    }

    public ThreadTask create() {
        Runnable runnable = actionCreator.get(resourceMessageCreator);

        return new ThreadTask(taskType, runnable);
    }

}
