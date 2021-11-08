package reader;

import httpspec.HttpMethod;

import java.io.InputStream;
import java.util.Set;

public class HttpRequest {
    HttpStartLine httpStartLine;
    HttpHeader httpHeader;

    public HttpRequest(InputStream inputStream) {
        httpStartLine = new HttpStartLine(inputStream);
        httpHeader = new HttpHeader(inputStream);
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

    public String getHeaderValue(String key){
        return httpHeader.getValue(key);
    }

    public Set<String> headerKeySet(){
        return httpHeader.keySet();
    }

    public boolean headerContainKey(String key){
        return httpHeader.containKey(key);
    }

    public String getValue(String key){
        return httpHeader.getValue(key);
    }
}
