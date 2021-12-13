package response.message.content;

import domain.FileExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;

class FileMessageTest {

    @DisplayName("contentType 이 올바른 값이 반환되야 합니다.")
    @ParameterizedTest
    @CsvSource(value = {".html, text/html", ".htm, text/html", ".txt, text/plain", ".java, text/plain"})
    void getContentType(String type, String text) {
        //given
        File file = new File(type);
        FileExtension fileExtension = FileExtension.parse(type);

        FileMessage fileMessage = new FileMessage(fileExtension, file);

        //when
        String actual = fileMessage.getContentType();

        //then
        Assertions.assertThat(actual).isEqualTo(text);
    }

}