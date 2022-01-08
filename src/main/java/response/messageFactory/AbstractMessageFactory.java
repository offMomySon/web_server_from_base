package response.messageFactory;

import domain.ResourcePath;
import lombok.NonNull;
import response.message.sender.Message;

public interface AbstractMessageFactory {

    Message createMessage(@NonNull ResourcePath resourcePath);

    boolean isSupported(@NonNull ResourcePath resourcePath);
}
