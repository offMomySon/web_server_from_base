package response.sender;

import reader.httpspec.HttpRequest;

public abstract class RequestSender {
    public abstract void doProcess(String hostAddress, HttpRequest httpRequest);
}
