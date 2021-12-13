package domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;
import java.util.stream.Stream;

public enum FileExtension {
    HTML(".html", "text/html"), HTM(".htm", "text/html"),
    TXT(".txt", "text/plain"), JAVA(".java", "text/plain"),
    GIF(".gif", "image/gif"), PNG(".png", "image/png"),
    CLASS(".class", "application/octet-stream"),
    JPG(".jpg", "image/jpeg"), JPEG(".jpeg", "image/jpeg"),
    MPEG(".mpeg", "video/mpeg"), TS(".ts", "video/MP2T"), AVI(".avi", "video/x-msvideo"),
    UNKNOWN("", "text/plain");

    private final String value;
    private final String contentType;

    FileExtension(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    @JsonCreator
    public static FileExtension parse(String extension) {
        String _extension = validate(extension);

        FileExtension[] fileExtensionTypes = values();

        return Stream.of(fileExtensionTypes)
                .filter(f -> f.value.equals(_extension.toLowerCase()))
                .findFirst()
                .orElse(UNKNOWN);
    }

    private static String validate(String extension) {
        if (Objects.isNull(extension)) {
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }
        if (!extension.startsWith(".")) {
            throw new IllegalArgumentException("첫번째 문자는 '.' 이어야 합니다");
        }
        if (extension.length() <= 1) {
            throw new IllegalArgumentException("문자의 길이는 최소 2이상 이어야 합니다.");
        }
        if (extension.contains(" ")) {
            throw new IllegalArgumentException("확장자에 ' ' 가 포함되면 안됩니다.");
        }
        return extension;
    }

    public String getContentType() {
        return this.contentType;
    }
}
