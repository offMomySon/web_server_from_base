package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import config.ConfigManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Getter
@ToString
@Slf4j
public class ResourcePath {
    private final Path value;

    private ResourcePath(@NonNull Path value) {
        this.value = removeRelativePath(value);
    }

    @JsonCreator
    public static ResourcePath create(String value) {
        return new ResourcePath(Paths.get(value));
    }

    private static Path removeRelativePath(Path requestTarget) {
        Stack<String> pathStack = new Stack<>();
        String[] splitPath = requestTarget.toString().split("/");

        for (String path : splitPath) {
            if (path.length() == 0) {
                continue;
            }

            if (!path.equals("..")) {
                pathStack.push(path);
                continue;
            }

            if (pathStack.isEmpty()) {
                throw new RuntimeException("상대경로를 벗어나면 안됩니다.");
            }
            pathStack.pop();
        }

        return Paths.get("/" + String.join("/", pathStack));
    }

    public ResourcePath append(ResourcePath resourcePath) {
        if (Objects.isNull(resourcePath)) {
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }

        StringBuilder newPath = new StringBuilder("");

        newPath.append(value.toString()).append(resourcePath.value.toString());

        return ResourcePath.create(newPath.toString());
    }

    public boolean exists() {
        ResourcePath resourcePath = ConfigManager.getInstance().getDownloadConfig().getResourcePath(this);
        return Files.exists(resourcePath.value);
    }

    public boolean isFile() {
        ResourcePath resourcePath = ConfigManager.getInstance().getDownloadConfig().getResourcePath(this);
        return Files.isRegularFile(resourcePath.value);
    }

    public boolean isDirectory() {
        ResourcePath resourcePath = ConfigManager.getInstance().getDownloadConfig().getResourcePath(this);
        return Files.isDirectory(resourcePath.value);
    }

    public FileExtension createFileExtension() {
        String fileName = value.getFileName().toString();
        fileName = fileName.substring(fileName.lastIndexOf("."));

        return FileExtension.parse(fileName);
    }

    public File createDownloadFile() {
        ResourcePath resourcePath = ConfigManager.getInstance().getDownloadConfig().getResourcePath(this);

        return resourcePath.value.toFile();
    }
}
