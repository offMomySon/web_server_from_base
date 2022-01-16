package action;

import domain.ResourcePath;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class CompositedActionCreator implements ActionCreator {
    private final List<ActionCreator> actionCreators;

    public CompositedActionCreator(@NonNull List<ActionCreator> actionCreators) {
        this.actionCreators = Collections.unmodifiableList(actionCreators);
    }

    @Override
    public Runnable get(@NonNull ResourcePath resourcePath) {
        Runnable newRunner = () -> {
        };

        for (ActionCreator actionCreator : actionCreators) {
            Runnable _originRunner = newRunner;

            newRunner = () -> {
                _originRunner.run();
                actionCreator.get(resourcePath).run();
            };
        }

        return newRunner;
    }
}
