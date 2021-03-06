package reader;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import reader.httpspec.startLine.HttpMethod;
import reader.httpspec.startLine.HttpStartLine;

class HttpStartLineTest {

  @DisplayName("start line 의 GET Method 를 올바르게 받는지 확인.")
  @ParameterizedTest
  @ValueSource(strings = {"GET / HTTP/1.1", "GET /tempDir HTTP/1.1",
      "GET /background.png HTTP/1.0"})
  void getHttpGetMethodTest(String startLine) {
    //given
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(
            new ByteArrayInputStream(startLine.getBytes(StandardCharsets.UTF_8)))));
    HttpStartLine httpStartLine = new HttpStartLine(bufferedReader);

    //when
    HttpMethod actual = httpStartLine.getHttpMethod();
    HttpMethod expected = HttpMethod.GET;

    //then
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("정상적이지 않은 start line 을 받으면 에러를 출력해야한다.")
  @ParameterizedTest
  @ValueSource(strings = {"GET /HTTP/1.1", "GET/tempDir HTTP/1.1", "GET /TEMP"})
  public void getHttpStartLineExceptionTest(String startLine) {
    //given
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(
            new ByteArrayInputStream(startLine.getBytes(StandardCharsets.UTF_8)))));

    //when
    Throwable thrown = catchThrowable(() -> {
      HttpStartLine httpStartLine = new HttpStartLine(bufferedReader);
    });

    //then
    Assertions.assertThat(thrown)
        .isInstanceOfAny(ArrayIndexOutOfBoundsException.class, IllegalArgumentException.class);
  }

  @DisplayName("start line 의 POST method 을 올바르게 받는지 확인.")
  @ParameterizedTest
  @ValueSource(strings = {"POST /temp HTTP/1.1", "POST /res HTTP/1.0"})
  void getHttpPostMethodTest(String startLine) {
    //given
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(
            new ByteArrayInputStream(startLine.getBytes(StandardCharsets.UTF_8)))));
    HttpStartLine httpStartLine = new HttpStartLine(bufferedReader);

    //when
    HttpMethod actual = httpStartLine.getHttpMethod();
    HttpMethod expected = HttpMethod.POST;

    //then
    Assertions.assertThat(actual).isEqualTo(expected);
  }
}