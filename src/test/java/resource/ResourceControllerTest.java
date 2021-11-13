package resource;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ResourceControllerTest {

  @DisplayName("접근가능한 url 을 받으면, 올바른 filePath 를 반환해야한다.")
  @ParameterizedTest
  @MethodSource("provideRequestTarget")
  void getFilePathTest(String testTarget, String expect) {
    //given
    ResourceController resourceController = new ResourceController("");

    //when
    String actual = resourceController.getFilePath(testTarget);

    //then
    Assertions.assertThat(actual).isEqualTo(expect);
  }

  private static Stream<Arguments> provideRequestTarget() {
    return Stream.of(
        Arguments.of("/tmp/../test", "/test"),
        Arguments.of("//tmp/test", "/tmp/test"),
        Arguments.of("//tmp/../test", "/test")
    );
  }

  @DisplayName("접근 불가능한 url 을 받으면, runtime eqxception 이 발생해야한다.")
  @ParameterizedTest
  @ValueSource(strings = {"/tmp/../../test", "/tmp/tmp2/../../../test", "/tmp/../.."})
  void getFilePathTest(String testTarget) {
    //given
    ResourceController resourceController = new ResourceController("");

    //when
    Throwable throwable = catchThrowable(() -> {
      String actual = resourceController.getFilePath(testTarget);
    });

    //then
    Assertions.assertThat(throwable).isInstanceOf(RuntimeException.class);
  }
}