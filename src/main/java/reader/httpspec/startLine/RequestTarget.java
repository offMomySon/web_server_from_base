package reader.httpspec.startLine;

import lombok.extern.slf4j.Slf4j;
import util.FileExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
public class RequestTarget {
    private final Path value;

    private RequestTarget(String value) {
        this.value = validate(value);
    }

    public static RequestTarget create(String value){
        return new RequestTarget(value);
    }

    private Path validate(String value){
        Path path;

        try {
            path = Paths.get(removeRelativePath(Paths.get(value).toString()));
        } catch (Exception e) {
            throw new IllegalArgumentException("상대경로를 벗어나는 경로를 받으면 안됩니다.");
        }

        return path;
    }

    //네이밍이 이게 맞나?
    public FileExtension getFileExteinsion(){
        return Optional.of(value.getFileName().toString())
                .map(s -> s.substring(s.lastIndexOf(".")))
                .map(s-> new FileExtension(s))
                .orElseThrow(() -> new IllegalArgumentException("FileExtension 생성 실패."));
    }

    private String removeRelativePath(String requestTarget) throws Exception {
        Stack<String> pathStack = new Stack<>();
        String[] splitPath = requestTarget.split("/");

        for (String path : splitPath) {
            if (path.length() == 0) {
                continue;
            }

            if (path.equals("..")) {
                if (pathStack.isEmpty()) {
                    throw new Exception("상대경로를 벗어나면 안됩니다.");
                }
                pathStack.pop();
            } else {
                pathStack.push(path);
            }
        }

        return pathStack.stream().collect(Collectors.joining("/", "/", ""));
    }

}
