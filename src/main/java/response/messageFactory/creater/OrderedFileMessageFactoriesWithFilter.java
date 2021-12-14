package response.messageFactory.creater;

import response.messageFactory.AbstractMessageFactory;
import response.messageFactory.RestrictedExtensionAtIpMessageFactory;
import response.messageFactory.RestrictedExtensionMessageFactory;

import java.util.List;

public class OrderedFileMessageFactoriesWithFilter {

    public List<AbstractMessageFactory> create() {
        return List.of(
                new RestrictedExtensionAtIpMessageFactory(),
                new RestrictedExtensionMessageFactory()
        );
    }
}
