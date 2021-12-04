package reader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import reader.httpspec.HttpBody;

class HttpBodyTest {

  @DisplayName("Body 를 올바르게 가져와야 한다.")
  @ParameterizedTest
  @MethodSource("provideHttpBodyAndExpect")
  void getHttpBodyTest(String testBody, String expect) {
    //given
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(new BufferedInputStream(
            new ByteArrayInputStream(testBody.getBytes(StandardCharsets.UTF_8)))));
    HttpBody httpBody = new HttpBody(bufferedReader, testBody.length());

    //when
    String actualBody = httpBody.getBody();

    //then
    Assertions.assertThat(actualBody).isEqualTo(expect);
  }

  private static Stream<Arguments> provideHttpBodyAndExpect() {
    String provide = "{" +
        "'FirstName':'value',\n" +
        "'LastName':'last',\n" +
        "'UserName':'name',\n" +
        "'Password':'passw',\n" +
        "'Email':'test@naver.com'\n" +
        "}";
    String expect = "{" +
        "'FirstName':'value',\n" +
        "'LastName':'last',\n" +
        "'UserName':'name',\n" +
        "'Password':'passw',\n" +
        "'Email':'test@naver.com'\n" +
        "}";
    return Stream.of(Arguments.of(provide, expect));
  }
}