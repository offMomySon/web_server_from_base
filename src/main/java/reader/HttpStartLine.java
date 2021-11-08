package reader;

import httpspec.HttpMethod;
import lombok.Getter;
import util.IoUtil;

import java.io.*;

import static util.IoUtil.*;

@Getter
public class HttpStartLine {
    private final HttpMethod httpMethod;
    private final String requestTarget;
    private final String httpVersion;

    public HttpStartLine(InputStream inputStream) {
        BufferedReader reader = createReader(inputStream);

        try {
            String startLine = reader.readLine();

            httpMethod = HttpMethod.valueOf(startLine.split(" ")[0]);
            requestTarget = startLine.split(" ")[1];
            httpVersion = startLine.split(" ")[2];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
