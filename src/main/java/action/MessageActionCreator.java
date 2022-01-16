package action;

import lombok.NonNull;
import response.message.sender.Message;
import response.messageFactory.AbstractMessageFactory;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class MessageActionCreator extends MainTaskActionCreator {

    public MessageActionCreator(@NonNull AbstractMessageFactory targetMessageFactory, @NonNull Function<Message, Runnable> messageHandler) {
        super(targetMessageFactory, messageHandler);
    }

    public static void sendMessage(@NonNull Socket socket, @NonNull Message message) {
        String content = message.create();

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageActionCreator create(@NonNull AbstractMessageFactory targetMessageFactory, @NonNull Socket socket) {
        return new MessageActionCreator(
                targetMessageFactory,
                (message) -> () -> sendMessage(socket, message)
        );
    }
}
