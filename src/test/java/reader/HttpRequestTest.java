package reader;

import static org.junit.jupiter.api.Assertions.*;

import httpspec.HttpMethod;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class HttpRequestTest {

  @DisplayName("httpRequest 가 생성되는지 확인.")
  @ParameterizedTest
  @MethodSource("provideHttpRequest")
  void getHttpGetMethodTest(String httpRquest) {
    //given
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(httpRquest.getBytes(StandardCharsets.UTF_8));

    //when
    HttpRequest httpRequest = new HttpRequest(byteArrayInputStream);

    //then
    Assertions.assertThat(true).as("failure - should be true").isTrue();
  }


  private static Stream<Arguments> provideHttpRequest() {
    String httpGetRequest = "GET /testPath HTTP/1.1\n"
        + "Host: localhost:5001\n" +
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
        "\n";
    String httpPostRequest = "POST / HTTP/1.1\n"
        + "Host: localhost:5001\n"
        + "Connection: keep-alive\n"
        + "Content-Length: 118\n"
        + "sec-ch-ua-mobile: ?0\n"
        + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36\n"
        + "sec-ch-ua: \"Chromium\";v=\"94\", \" Not A;Brand\";v=\"99\", \"Whale\";v=\"2\"\n"
        + "sec-ch-ua-platform: \"Windows\"\n"
        + "Content-Type: application/json\n"
        + "Accept: */*\n"
        + "Sec-Fetch-Site: none\n"
        + "Sec-Fetch-Mode: cors\n"
        + "Sec-Fetch-Dest: empty\n"
        + "Accept-Encoding: gzip, deflate, br\n"
        + "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
        "\n" +
        "{\n" +
        "\"FirstName\":\"value\",\n" +
        "\"LastName\":\"valu\",\n" +
        "\"UserName\": \"valu\",\n" +
        "\"Password\" : \"value\",\n" +
        "\"Email\" : \"Value\"\n" +
        "}";

    return Stream.of(
        Arguments.of(httpGetRequest),
        Arguments.of(httpPostRequest)
    );
  }
}