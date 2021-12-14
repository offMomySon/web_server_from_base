package response.messageFactory;

import domain.ResourcePath;
import response.message.sender.Message;

import java.util.List;

public class CompositedFileMessageFactoryWithFilter implements AbstractMessageFactory {

    private final List<AbstractMessageFactory> fileMessageFactories;

    public CompositedFileMessageFactoryWithFilter(List<AbstractMessageFactory> fileMessageFactories) {
        this.fileMessageFactories = fileMessageFactories;
    }

    @Override
    public Message createMessage(String hostAddress, ResourcePath resourcePath) {
        return fileMessageFactories.stream()
                .filter(e -> e.isSupported(hostAddress, resourcePath))
                .map(e -> e.createMessage(hostAddress, resourcePath))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean isSupported(String hostAddress, ResourcePath resourcePath) {
        return fileMessageFactories.stream().anyMatch(f -> f.isSupported(hostAddress, resourcePath));
    }
}
