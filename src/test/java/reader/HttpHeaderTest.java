package reader;

import httpspec.HttpMethod;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.*;

class HttpHeaderTest {

  @DisplayName("올바르게 header 를 가져와야한다.")
  @ParameterizedTest
  @MethodSource("provideHttpHeaderAndParsedMap")
  void httpHeaderGetTest(String httpHeaderStr, Map<String, String> expect) {
    //given
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        httpHeaderStr.getBytes(StandardCharsets.UTF_8));
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(byteArrayInputStream)));
    HttpHeader httpHeader = new HttpHeader(bufferedReader);

    //when
    //then
    SoftAssertions softly = new SoftAssertions();
    for (String actualKey : httpHeader.keySet()) {
      softly.assertThat(httpHeader.getValue(actualKey)).as("check key %s's value", actualKey)
          .isEqualTo(expect.get(actualKey));
    }
    softly.assertAll();
  }

  private static Stream<Arguments> provideHttpHeaderAndParsedMap() {
    return Stream.of(
        Arguments.of("Host: localhost:5001\n" +
            "Connection: keep-alive\n" +
            "sec-ch-ua-mobile: ?0\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36\n"
            +
            "sec-ch-ua-platform: \"Windows\"\n" +
            "sec-ch-ua: \"Chromium\";v=\"94\", \" Not A;Brand\";v=\"99\", \"Whale\";v=\"2\"\n" +
            "Accept: */*\n" +
            "Sec-Fetch-Site: none\n" +
            "Sec-Fetch-Mode: cors\n" +
            "Sec-Fetch-Dest: empty\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
            "\n", new HashMap<>() {{
          put("Host", "localhost:5001");
          put("Connection", "keep-alive");
          put("sec-ch-ua-mobile", "?0");
          put("User-Agent",
              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36");
          put("sec-ch-ua-platform", "\"Windows\"");
          put("sec-ch-ua", "\"Chromium\";v=\"94\", \" Not A;Brand\";v=\"99\", \"Whale\";v=\"2\"");
          put("Accept", "*/*");
          put("Sec-Fetch-Site", "none");
          put("Sec-Fetch-Mode", "cors");
          put("Sec-Fetch-Dest", "empty");
          put("Accept-Encoding", "gzip, deflate, br");
          put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        }})
    );
  }

}