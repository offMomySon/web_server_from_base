package domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ResourcePathTest {

    @DisplayName("생성시, 인자가 null 이면 안됩니다.")
    @Test
    void argumentNotNull() {
        //given
        String nullPath = null;

        //when
        Throwable throwable = Assertions.catchThrowable(() -> {
            ResourcePath resourcePath = ResourcePath.create(nullPath);
        });

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("생성시, 인자로 받은 Path 내에서만 탐색해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"../test1", "/test2/../../t2.ppt", "/test3/test3/../../../", "/test4/../test4_1/../test4_2/../../"})
    void overflowRelativePath(String path) {
        //given
        //when
        Throwable throwable = Assertions.catchThrowable(() -> {
            ResourcePath resourcePath = ResourcePath.create(path);
        });

        //then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적인 인자를 받으면 RequestTarget 객체를 생성합니다.")
    @ParameterizedTest
    @ValueSource(strings = {" test/sdgasdg///tets.txt ", "/test2/../test2_1/t.ppt"})
    void possiblePath(String path) {
        //given
        //when
        ResourcePath resourcePath = ResourcePath.create(path);

        //then
        Assertions.assertThat(true).as("failure - should be true").isTrue();
    }

    @DisplayName("RequestTarget 에서 FileExtension 을 만들수 있습니다.")
    @ParameterizedTest
    @CsvSource(value = {"/file/image.jpg, .jpg", "file/image.jpg, .jpg", "file/image.test.jpg, .jpg", "file//depth1///tem.jpg, .jpg"})
    void getFileExteinsion(String path, String extension) {
        //given
        ResourcePath resourcePath = ResourcePath.create(path);
        FileExtension expected = FileExtension.parse(extension);

        //when
        FileExtension actual = resourcePath.createFileExtension();

        //then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("시스템 내에 존재하는 파일이면 True 값을 출력해야합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/textFiles/test1.txt", "/textFiles/test2.bat"})
    void exists(String relativePath) {
        //given
        Path newFile = ResourcePath.create(relativePath).createDownloadFile().toPath();
        try {
            Files.createFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResourcePath resourcePath = ResourcePath.create(relativePath);

        //when
        boolean actual = resourcePath.exists();

        //then\
        Assertions.assertThat(actual).isEqualTo(true);
        try {
            Files.delete(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("파일이면 True 값을 출력해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/textFiles/test1_file.txt", "/textFiles/test2_file.bat"})
    void isFile(String relativePath) {
        //given
        Path newFile = ResourcePath.create(relativePath).createDownloadFile().toPath();
        try {
            Files.createFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResourcePath resourcePath = ResourcePath.create(relativePath);

        //when
        boolean actual = resourcePath.isFile();

        try {
            Files.delete(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("디렉토리면 True 값을 출력해야 합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/test1/", "/test2"})
    void isDirectory(String relativePath) {
        //given
        Path newDirectory = ResourcePath.create(relativePath).createDownloadFile().toPath();
        try {
            Files.createDirectories(newDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResourcePath fullResourcePath = ResourcePath.create(relativePath);

        //when
        boolean actual = fullResourcePath.isDirectory();

        try {
            Files.delete(newDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("같은 path 를 가지면 동등한 RequestTarget 입니다.")
    @ParameterizedTest
    @ValueSource(strings = {"/file/test.jpg"})
    void equals(String path) {
        //given
        ResourcePath resourcePath_1 = ResourcePath.create(path);
        ResourcePath resourcePath_2 = ResourcePath.create(path);

        //when
        boolean actual = resourcePath_1.equals(resourcePath_2);

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @DisplayName("append 하면 RequestTarget 의 value 에 추가됩니다.")
    @ParameterizedTest
    @CsvSource(value = {"/file/, test.jpg, /file/test.jpg"})
    void append(String basePath, String targetPath, String resultPath) {
        //given
        ResourcePath appendedTarget = ResourcePath.create(basePath).append(ResourcePath.create(targetPath));
        ResourcePath fullResourcePath = ResourcePath.create(resultPath);

        //when
        boolean actual = appendedTarget.equals(fullResourcePath);

        //then
        Assertions.assertThat(actual).isTrue();
    }
}
