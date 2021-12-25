package domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileExtensionTest {

    @DisplayName("확장자의 첫 문자는 '.' 입니다.")
    @Test
    void argumentNotNull() {
        //given
        String nullExtension = null;

        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            FileExtension.parse(nullExtension);
        });

        //then
        Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자의 첫 문자는 '.' 입니다.")
    @ParameterizedTest
    @ValueSource(strings = {"jpg", "mpg", "pdf"})
    void testFirstChar(String extensionWithoutDot) {
        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            FileExtension.parse(extensionWithoutDot);
        });

        //then
        Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자의 최소 길이는 2 입니다.")
    @ParameterizedTest
    @ValueSource(strings = {".",})
    void testLength(String extensionLessThanTwo) {
        //given
        //when
        Throwable thrown = Assertions.catchThrowable(() -> {
            FileExtension.parse(extensionLessThanTwo);
        });

        //then
        Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자에 ' ' 가 포함되면 안됩니다.")
    @ParameterizedTest
    @ValueSource(strings = {".p pt", ".dm g"})
    void testBlank(String extensionWithBlank) {
        //given
        //when
        Throwable throwable = Assertions.catchThrowable(() -> {
            FileExtension.parse(extensionWithBlank);
        });

        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 확장자를 가지면 같은 객체입니다.")
    @ParameterizedTest
    @ValueSource(strings = {".ppt", ".dmg"})
    void testEquals(String extension) {
        //given
        FileExtension fileExtension1 = FileExtension.parse(extension);
        FileExtension fileExtension2 = FileExtension.parse(extension);

        //when
        boolean isEquals = fileExtension1.equals(fileExtension2);

        //then
        Assertions.assertThat(isEquals).isTrue();
    }
}