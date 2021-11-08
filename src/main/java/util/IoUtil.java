package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IoUtil {

    public static BufferedReader createReader(InputStream inputStream){
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));
    }

}
