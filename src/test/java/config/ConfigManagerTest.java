package config;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ConfigManagerTest {

  @DisplayName("올바른 데이터를 받으면, ConfigManager 가 정상적으로 생성되어야 합니다.")
  @ParameterizedTest
  @MethodSource("provideConfigString")
  void createConfigManagerTest(String basicConfig, String downloadConfig, String threadConfig) {
    //given
    DataInputStream dataInputStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(basicConfig.getBytes(StandardCharsets.UTF_8))));
    DataInputStream downloadStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(downloadConfig.getBytes(StandardCharsets.UTF_8))));
    DataInputStream threadStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(threadConfig.getBytes(StandardCharsets.UTF_8))));

    //when
    ConfigManager configManager = ConfigManager.getInstance();

    //then
    Assertions.assertThat(true).as("failure - should be true").isTrue();
  }

  private static Stream<Arguments> provideConfigString() {
    return Stream.of(
        Arguments.of(
            "{\n" +
                "  \"port\": 5001,\n" +
                "  \"welcomPagePath\": \"src/main/resources/files/welcomePage.html\"\n" +
                "}",

            "{\n" +
                "  \"downloadPath\": \"src/main/resources/files\",\n" +
                "  \"period\": 1,\n" + "  \"count\": 1,\n"
                + "  \"restrictedFileExtension\": [\n"
                + "    \".jpg\",\n"
                + "    \".class\"\n"
                + "  ],\n"
                + "  \"restrictedFileExtensionAtIps\": [\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.1\",\n"
                + "      \"restrictedFileExtension\": [\n"
                + "        \".jpg\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.2\",\n"
                + "      \"restrictedFileExtension\": [\n"
                + "        \".class\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"periodCountConfigAtIps\": [\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.1\",\n"
                + "      \"period\": 1,\n"
                + "      \"count\": 1\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            "{\n"
                + "  \"usableThreadCount\": 5,\n"
                + "  \"waitableThreadCount\": 5\n"
                + "}"
        )
    );
  }

  @DisplayName("잘못된 데이터를 받음면, ConfigManager 생성시 예외가 발생해야 합니다.")
  @ParameterizedTest
  @MethodSource("provideWrongConfigString")
  void createWrongConfigManagerTest(String basicConfig, String downloadConfig,
      String threadConfig) {
    //given
    DataInputStream dataInputStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(basicConfig.getBytes(StandardCharsets.UTF_8))));
    DataInputStream downloadStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(downloadConfig.getBytes(StandardCharsets.UTF_8))));
    DataInputStream threadStream =
        new DataInputStream(new BufferedInputStream(
            new ByteArrayInputStream(threadConfig.getBytes(StandardCharsets.UTF_8))));

    //when
    Throwable thrown = catchThrowable(() -> {
      ConfigManager configManager = ConfigManager.getInstance();
    });

    //then
    Assertions.assertThat(thrown)
        .isInstanceOfAny(RuntimeException.class, ArrayIndexOutOfBoundsException.class,
            IllegalArgumentException.class);
  }

  private static Stream<Arguments> provideWrongConfigString() {
    return Stream.of(
        Arguments.of(
            "{\n" +
                "  \"port\": 50i01,\n" +
                "  \"welcomPagePath\": \"src/main/resources/files/welcomePage.html\"\n" +
                "}",

            "{\n" +
                "  \"downloadPath\": \"src/main/resources/files\",\n" +
                "  \"period\": 1,\n" + "  \"count\": 1,\n"
                + "  \"restrictedFileExtension\": [\n"
                + "    \".jpg\",\n"
                + "    \".class\"\n"
                + "  ],\n"
                + "  \"restrictedFileExtensionAtIps\": [\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.1\",\n"
                + "      \"restrictedFileExtension\": [\n"
                + "        \".jpg\"\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.2\",\n"
                + "      \"restrictedFileExtension\": [\n"
                + "        \".class\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"periodCountConfigAtIps\": [\n"
                + "    {\n"
                + "      \"ip\": \"192.168.0.1\",\n"
                + "      \"period\": 1,\n"
                + "      \"count\": 1\n"
                + "    }\n"
                + "  ]\n"
                + "}",
            "{\n"
                + "  \"usableThreadCount\": 5,\n"
                + "  \"waitableThreadCount\": 5\n"
                + "}"
        )
    );
  }

}