package response.message.sender;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public abstract class Message {
    private static final String END_OF_LINE = "\r\n";
    protected final StringBuilder content = new StringBuilder();
    private final StringBuilder headerBuilder = new StringBuilder();

    private String createHeader(long contentLength) {
        headerBuilder.append("HTTP/1.1 200 OK ").append(END_OF_LINE);
        headerBuilder.append("Content-Length : ").append(contentLength).append(END_OF_LINE);
        headerBuilder.append("Content-Type: ").append(getContentType()).append(END_OF_LINE);
        headerBuilder.append("Date: ").append(new Date()).append(END_OF_LINE);
        headerBuilder.append("Server: jihun server 1.0 ").append(END_OF_LINE);
        headerBuilder.append(END_OF_LINE);

        return headerBuilder.toString();
    }

    public String create() {
        String content = getContent();
        String header = createHeader(content.length());

        return header + content + END_OF_LINE;
    }

    protected abstract String getContentType();

    protected abstract String getContent();
}
