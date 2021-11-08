package reader;

import httpspec.HttpMethod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

class HttpStartLineTest {

    @DisplayName("start line 의 GET Method 를 올바르게 받는지 확인.")
    @ParameterizedTest
    @ValueSource(strings = {"GET / HTTP/1.1", "GET /tempDir HTTP/1.1", "GET /background.png HTTP/1.0"})
    void getHttpGetMethodTest(String startLine){
        //given
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(startLine.getBytes(StandardCharsets.UTF_8));
        HttpStartLine httpStartLine = new HttpStartLine(byteArrayInputStream);

        //when
        HttpMethod actual = httpStartLine.getHttpMethod();
        HttpMethod expected = HttpMethod.GET;

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("start line 의 POST method 을 올바르게 받는지 확인.")
    @ParameterizedTest
    @ValueSource(strings = {"POST /temp HTTP/1.1", "POST /res HTTP/1.0" })
    void getHttpPostMethodTest(String startLine){
        //given
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(startLine.getBytes(StandardCharsets.UTF_8));
        HttpStartLine httpStartLine = new HttpStartLine(byteArrayInputStream);

        //when
        HttpMethod actual = httpStartLine.getHttpMethod();
        HttpMethod expected = HttpMethod.POST;

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}