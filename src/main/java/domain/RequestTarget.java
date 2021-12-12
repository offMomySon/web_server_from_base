package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import config.ConfigManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    // [jihun]
    // domain 객체에 Path 를 담은게 실수인가.
    // Path 를 값으로 담아서 domain 객체의 기능이 너무 많아진것 같다..
    private final Path value;

    @JsonCreator
    private RequestTarget(String value) {
        this.value = validate(value);
    }

    private Path validate(String value){
        Path path;

        if(Objects.isNull(value)){
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }
        try {
            path = Paths.get(removeRelativePath(Paths.get(value).toString()));
        } catch (Exception e) {
            throw new IllegalArgumentException("상대경로를 벗어나, download config 값을 침범하는 path 를 받으면 안됩니다.");
        }

        return path;
    }
    public static RequestTarget create(String value){
        return new RequestTarget(value);
    }

    //generic 으로 더 유연하게 풀 수 있지 않을까?
    public RequestTarget append(RequestTarget requestTarget){
        // 여기까지 null 방어할 필요가 있을까?
        // 어차피 RequestTarget 을 다시 만들면서
        if(Objects.isNull(requestTarget)){
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }

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

    // 얘도 테스트 케이스 만들 필요가 있을까?
    public DownloadTarget getDownloadTarget() {
        return Optional.of(value.toString())
                .map(value->Path.of(value.toString()))
                .map(path -> DownloadTarget.create(path))
                .orElseThrow(()-> {throw new IllegalArgumentException("DownloadTarget 생성실패");});
    }

    //네이밍이 이게 맞나?
    public FileExtension getFileExtension(){
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

    // param 의 string 으로 던져줘야하는 경우가 있어서 이렇게 처리.
    // domain 객체를 덜 만들어줘서 그런가..
    @Override
    public String toString() {
        return value.toString();
    }
}
