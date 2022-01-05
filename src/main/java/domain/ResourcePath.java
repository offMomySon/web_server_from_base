package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import config.ConfigManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Stack;

@EqualsAndHashCode
@Getter
@ToString
@Slf4j
public class ResourcePath {
    private final Path value;

    // domain r객체의 validation 규칙 세워야함. /
    @JsonCreator
    private ResourcePath(Path value) {
        this.value = value;
    }

    //1
    public static ResourcePath create(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }

        try {
            return new ResourcePath(Paths.get(removeRelativePath(Paths.get(value).toString())));
        } catch (Exception e) {
            throw new IllegalArgumentException("상대경로를 벗어나, download config 값을 침범하는 path 를 받으면 안됩니다.");
        }
    }

    private static String removeRelativePath(String requestTarget) throws Exception {
        Stack<String> pathStack = new Stack<>();
        String[] splitPath = requestTarget.split("/");

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


//            if (path.equals("..")) {
//                if (pathStack.isEmpty()) {
//                    throw new Exception("상대경로를 벗어나면 안됩니다.");
//                }
//                pathStack.pop();
//            } else {
//                pathStack.push(path);
//            }
        }

        return "/" + String.join("/", pathStack);
//        return pathStack.stream().collect(Collectors.joining("/", "/", ""));
    }

    //2
    public Path get() {
        return value;
    }

    public ResourcePath append(ResourcePath resourcePath) {
        if (Objects.isNull(resourcePath)) {
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }

        StringBuilder newPath = new StringBuilder("");

        newPath.append(value.toString()).append(resourcePath.value.toString());

        return ResourcePath.create(new StringBuilder()
                .append(value)
                .append(resourcePath.value).toString());
//        return ResourcePath.create(newPath.toString());
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

    // 2
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
