package reader.httpspec.startLine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import util.FileExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RequestTargetTest {

    @DisplayName("인자로 받은 Path 내에서만 탐색해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings={"../test1", "/test2/../../t2.ppt", "/test3/test3/../../../","/test4/../test4_1/../test4_2/../../"})
    void overflowRelativePath(String path) {
        //given
        //when
        Throwable throwable = Assertions.catchThrowable(()->{
            RequestTarget requestTarget = RequestTarget.create(path);
        });

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적인 인자를 받으면 RequestTarget 객체를 생성합니다.")
    @ParameterizedTest
    @ValueSource(strings={" test/sdgasdg///tets.txt ", "/test2/../test2_1/t.ppt"})
    void possiblePath(String path) {
        //given
        //when
        RequestTarget requestTarget = RequestTarget.create(path);

        //then
        Assertions.assertThat(true).as("failure - should be true").isTrue();
    }

    @DisplayName("sdfsdf")
    @ParameterizedTest
    @CsvSource(value = {"/file/image.jpg, .jpg", "file/image.jpg, .jpg", "file/image.test.jpg, .jpg", "file//depth1///tem.jpg, .jpg"})
    void getFileExteinsion(String path, String extension){
        //given
        RequestTarget requestTarget = RequestTarget.create(path);
        FileExtension expected = new FileExtension(extension);

        //when
        FileExtension actual = requestTarget.getFileExteinsion();

        //then
        Assertions.assertThat(expected).isEqualTo(actual);
    }

}