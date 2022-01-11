package Sequence;

import domain.ResourcePath;
import lombok.NonNull;

public interface TaskRunner {
    void doRun(@NonNull ResourcePath resourcePath);

    boolean isPossibleRun(@NonNull ResourcePath resourcePath);
}
