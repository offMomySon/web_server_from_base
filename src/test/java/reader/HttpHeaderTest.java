package reader;

import httpspec.HttpMethod;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.entry;


import static org.junit.jupiter.api.Assertions.*;

class HttpHeaderTest {

    @DisplayName("올바르게 header 를 가져와야한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Host: localhost:5001\n" +
            "Connection: keep-alive\n" +
            "sec-ch-ua-mobile: ?0\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36\n" +
            "sec-ch-ua-platform: \"Windows\"\n" +
            "sec-ch-ua: \"Chromium\";v=\"94\", \" Not A;Brand\";v=\"99\", \"Whale\";v=\"2\"\n" +
            "Accept: */*\n" +
            "Sec-Fetch-Site: none\n" +
            "Sec-Fetch-Mode: cors\n" +
            "Sec-Fetch-Dest: empty\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
            "\n"})
    void httpHeaderGetTest(String headerLines){
        //given
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(headerLines.getBytes(StandardCharsets.UTF_8));
        HttpHeader httpHeader = new HttpHeader(byteArrayInputStream);

        //when
        Set<String> headerKeys = httpHeader.keySet();
        HashMap<String, String> expect = new HashMap<>();
        expect.put("Host", "localhost:5001");
        expect.put("Connection", "keep-alive");
        expect.put("sec-ch-ua-mobile", "?0");
        expect.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36");
        expect.put("sec-ch-ua-platform","\"Windows\"");
        expect.put("sec-ch-ua", "\"Chromium\";v=\"94\", \" Not A;Brand\";v=\"99\", \"Whale\";v=\"2\"");
        expect.put("Accept", "*/*");
        expect.put("Sec-Fetch-Site", "none");
        expect.put("Sec-Fetch-Mode", "cors");
        expect.put("Sec-Fetch-Dest", "empty");
        expect.put("Accept-Encoding", "gzip, deflate, br");
        expect.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

        //then
        SoftAssertions softly = new SoftAssertions();
        for(String actualKey : headerKeys){
            softly.assertThat(httpHeader.getValue(actualKey)).isEqualTo(expect.get(actualKey));
        }
        softly.assertAll();
    }
}