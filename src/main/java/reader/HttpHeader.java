package reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static util.IoUtil.*;

public class HttpHeader {
    private final Map<String, String> header = new HashMap<>();
    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public HttpHeader(InputStream inputStream) {
        BufferedReader reader = createReader(inputStream);

        try {
            logger.info("start logging header");
            while(true){
                String headerLine = reader.readLine();
                logger.info(headerLine);

                if(headerLine.length() == 0){
                    break;
                }

                String[] headerPart = headerLine.split(":",2);
                if(headerPart.length == 2){
                    header.put(headerPart[0].trim(), headerPart[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> keySet(){
        return header.keySet();
    }

    public boolean containKey(String key){
        return header.containsKey(key);
    }

    public String getValue(String key){
        return header.get(key);
    }
}
