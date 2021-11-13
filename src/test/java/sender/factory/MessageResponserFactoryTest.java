package sender.factory;

import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import resource.ResourceStatus;
import sender.strategy.DirectoryMessageResponser;
import sender.strategy.FileMessageResponser;
import sender.strategy.MessageResponser;
import sender.strategy.NotFoundMessageResponser;

class MessageResponserFactoryTest {

  @DisplayName("Resource status 에 따라 의도한 MessageResponser 를 생성하는 지 확인.")
  @ParameterizedTest
  @MethodSource("provideResourceStatus")
  void getMessageREsponser(ResourceStatus resourceStatus, Class<MessageResponser> expect) {
    //given
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    String testFilePath = "test";

    //when
    MessageResponser actual = MessageResponserFactory
        .getMessageResponser(byteArrayOutputStream, resourceStatus, testFilePath);

    //then
    Assertions.assertThat(actual).isInstanceOf(expect);
  }

  private static Stream<Arguments> provideResourceStatus() {
    return Stream.of(
        Arguments.of(ResourceStatus.PATH_NOT_EXIST, NotFoundMessageResponser.class),
        Arguments.of(ResourceStatus.DIRECTORY_EXIST, DirectoryMessageResponser.class),
        Arguments.of(ResourceStatus.FILE_EXIST, FileMessageResponser.class)
    );
  }
}