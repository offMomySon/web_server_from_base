package response.messageFactory;

import domain.ResourcePath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import response.message.content.FileMessage;
import response.message.sender.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileMessageFactoryTest {

    @DisplayName("시스템 내에 존재하는 파일이면 True 값을 출력해야합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/textFiles/test1.txt", "/textFiles/test2.bat"})
    void isSupportPath(String relativePath) {
        //given
        Path newFile = ResourcePath.create(relativePath).createDownloadFile().toPath();
        try {
            Files.createFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileMessageFactory fileMessageFactory = new FileMessageFactory();

        //when
        boolean actual = fileMessageFactory.isSupported("", ResourcePath.create(relativePath));

        try {
            Files.delete(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        Assertions.assertThat(actual).isEqualTo(true);
    }

    @DisplayName("RequestTarget 을 받으면 Message 를 생성해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/textFiles/test1.txt", "/textFiles/test2.bat"})
    void createMessage(String relativePath) {
        //given
        Path newFile = ResourcePath.create(relativePath).createDownloadFile().toPath();
        try {
            Files.createFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResourcePath resourcePath = ResourcePath.create(relativePath);

        FileMessageFactory fileMessageFactory = new FileMessageFactory();

        //when
        Message message = fileMessageFactory.createMessage("", resourcePath);

        try {
            Files.delete(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        Assertions.assertThat(message).isInstanceOf(FileMessage.class);
    }
}