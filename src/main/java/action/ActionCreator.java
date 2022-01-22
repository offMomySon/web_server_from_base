package action;

import domain.ResourceMessageCreator;
import lombok.NonNull;

public interface ActionCreator {
    Runnable get(@NonNull ResourceMessageCreator resourceMessageCreator);
}
