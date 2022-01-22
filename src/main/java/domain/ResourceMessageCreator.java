package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import config.ConfigManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import response.message.content.DirectoryMessage;
import response.message.content.FileMessage;
import response.message.content.WelcomePageMessage;
import response.message.sender.Message;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

@EqualsAndHashCode
@Getter
@ToString
@Slf4j
public class ResourceMessageCreator {
    private final Path value;

    private ResourceMessageCreator(@NonNull Path value) {
        this.value = removeRelativePath(value);
    }

    @JsonCreator
    public static ResourceMessageCreator create(String value) {
        return new ResourceMessageCreator(Paths.get(value));
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

    public ResourceMessageCreator append(ResourceMessageCreator resourceMessageCreator) {
        if (Objects.isNull(resourceMessageCreator)) {
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }

        StringBuilder newPath = new StringBuilder("");

        newPath.append(value.toString()).append(resourceMessageCreator.value.toString());

        return ResourceMessageCreator.create(newPath.toString());
    }

    public Optional<Message> createMessage() {
        ResourceMessageCreator resourceMessageCreator = ConfigManager.getInstance().getDownloadConfig().getResourcePath(this);

        if (Files.exists(resourceMessageCreator.value)) {
            return Optional.of(new FileMessage(resourceMessageCreator.createFileExtension(), resourceMessageCreator.value.toFile()));
        }

        if (Files.isDirectory(resourceMessageCreator.value)) {
            return Optional.of(new DirectoryMessage(resourceMessageCreator.value.toString()));
        }

        if (ConfigManager.getInstance().isWelcomePage(resourceMessageCreator)) {
            return Optional.of(new WelcomePageMessage());
        }

        return Optional.empty();
    }

    public FileExtension createFileExtension() {
        String fileName = value.getFileName().toString();
        fileName = fileName.substring(fileName.lastIndexOf("."));

        return FileExtension.parse(fileName);
    }
}
