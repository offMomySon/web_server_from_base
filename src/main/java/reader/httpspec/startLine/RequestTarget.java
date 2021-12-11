package reader.httpspec.startLine;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.FileExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

//[jihun] domain 객체인데 기능이 너무 많은거 같다..
@Getter
@Slf4j
public class RequestTarget {
    //domain 객체에 Path 를 담은게 실수인가.
    // Path 를 값으로 담아서 domain 객체의 기능이 너무 많아진것 같다..
    private final Path value;

    @JsonCreator
    private RequestTarget(String value) {
        this.value = validate(value);
    }

    public static RequestTarget create(String value){
        return new RequestTarget(value);
    }

    //generic 으로 더 유연하게 풀 수 있지 않을까?
    public RequestTarget append(RequestTarget requestTarget){
        StringBuilder newPath = new StringBuilder("");

        newPath.append(value.toString()).append(requestTarget.getValue().toString());

        return RequestTarget.create(newPath.toString());
    }

    public boolean exists(){
        return Files.exists(value);
    }

    public boolean isFile(){
        return Files.isRegularFile(value);
    }

    public boolean isDirectory(){
        return Files.isDirectory(value);
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

    //param 의 string 으로 던져줘야하는 경우가 있어서 이렇게 처리.
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestTarget that = (RequestTarget) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
