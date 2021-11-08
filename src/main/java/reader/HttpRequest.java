package reader;

import httpspec.HttpMethod;

import java.io.InputStream;

public class HttpRequest {
    HttpStartLine httpStartLine;
    public HttpRequest(InputStream inputStream) {
        httpStartLine = new HttpStartLine(inputStream);
    }

    public HttpMethod getHttpMethod(){
        return httpStartLine.getHttpMethod();
    }

    public String getRequestTarget(){
        return httpStartLine.getRequestTarget();
    }

    public String getRequestVersion(){
        return httpStartLine.getHttpVersion();
    }
}
