package action;

import domain.ResourcePath;
import lombok.NonNull;

public interface ActionCreator {
    Runnable get(@NonNull ResourcePath resourcePath);
}
