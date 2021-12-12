package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

//Todo] domain 객체인데 기능이 너무 많은거 같다..
@Getter
@Slf4j
// http spec 을 얹어서 굳이 이름을 만들어야하나?
public class RequestTarget {
    // TODO
    // domain 객체에 Path 를 담은게 실수인가.
    // Path 를 값으로 담아서 domain 객체의 기능이 너무 많아진것 같다..
    private final Path value;

    // 생성자는 멤버변수 초기화만 해야한다.
    @JsonCreator
    private RequestTarget(String value) {
        this.value = validate(value);
    }

    // string 다시 변환 해야함.
    // 이건 convert
    // string q받으면 string  반환
    // 이름의 디테일 필요하다.
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
    // 시그니처 같으면 안만드는게 좋아
    // 생성자의 자료형을 유도하기 어려울때만 사용해야함.
    // 만들 필요없음.
    public static RequestTarget create(String value){
        return new RequestTarget(value);
    }

    // TODO
    //generic 으로 더 유연하게 풀 수 있지 않을까?
    //RequestTarget 대신 string 을 받는다거나.
    public RequestTarget append(@NonNull RequestTarget requestTarget){
        // 
        // 여기까지 null 방어할 필요가 있을까?
        // 어차피 RequestTarget 을 다시 만들면서 null 검사가 될텐데..
        // 하지만 로직 타기전에 null 검사가 좋기는 할듯
        // public 에서는 반드시. , NonNull 도 가능 하지만 메세지 충분하지 않음.
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

    // Todo
    // 얘도 테스트 케이스 만들 필요가 있을까?
    // 단순히 DownloadTarget 으로 매핑한는 건데
    // create
    // null 검사만, donwloadTarget 생성 검사는 downloadTarget 이 가져갈테니.
    public DownloadTarget getDownloadTarget() {

        // 가독성을 위해서 만들었는데
        // domain 객체를 이미 검증했기 때문에 null 검사 필요없다.
        // domain 객체의 장점을 누려야함.
        return Optional.ofNullable(value.toString())
                .map(value->Path.of(value.toString()))
                .map(path -> DownloadTarget.create(path))
                .orElseThrow(()-> {throw new IllegalArgumentException("DownloadTarget 생성실패");});
    }

    //네이밍이 별로인듯..
    public FileExtension createFileExtension(){
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

    // 롬복쓰자
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

    // Todo
    // param 의 string 으로 던져줘야하는 경우가 있어서 이렇게 처리.
    // domain 객체를 덜 만들어줘서 그런가..
    // 모든 로직에서 도메인 객체 그대로 사용해주는게 좋을거 같은데..

    // 로그의 목적으로만 사용함.
    // 특정 목적을 가지고 사용하면 안됨.
    // 왜 String 이 필요한지 생각해봐야함. -> 특정 파일의 binary 파일을 받기 위해서 -> File 을 던져줘야함.

    // 필요한 개념들을 return 해주는것을 생각해봐야함.
    // domain 객체의 장점이 무색해짐.
    @Override
    public String toString() {
        return value.toString();
    }
}
