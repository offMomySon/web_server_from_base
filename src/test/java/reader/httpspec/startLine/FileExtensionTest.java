package reader.httpspec.startLine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.FileExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileExtensionTest {

    @DisplayName("확장자의 첫 문자는 '.' 입니다.")
    @ParameterizedTest
    @ValueSource(strings={"jpg","mpg","pdf"})
    void testFirstChar(String extensionWithoutDot){
        //given
        //when
        Throwable thrown = Assertions.catchThrowable(()->{
            new FileExtension(extensionWithoutDot);
        });

        //then
        Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자의 최소 길이는 2 입니다.")
    @ParameterizedTest
    @ValueSource(strings={".",})
    void testLength(String extensionLessThanTwo){
        //given
        //when
        Throwable thrown = Assertions.catchThrowable(()->{
            new FileExtension(extensionLessThanTwo);
        });

        //then
        Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("확장자에 ' ' 가 포함되면 안됩니다.")
    @ParameterizedTest
    @ValueSource(strings = {".p pt", ".dm g"})
    void testBlank(String extensionWithBlank){
        //given
        //when
        Throwable throwable = Assertions.catchThrowable(()->{
           new FileExtension(extensionWithBlank);
        });

        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 확장자를 가지면 같은 객체입니다.")
    @ParameterizedTest
    @ValueSource(strings = {".ppt", ".dmg"})
    void testEquals(String extension){
        //given
        FileExtension fileExtension1 = new FileExtension(extension);
        FileExtension fileExtension2 = new FileExtension(extension);

        //when
        boolean isEquals = fileExtension1.equals(fileExtension2);

        //then
        Assertions.assertThat(isEquals).isTrue();
    }
}