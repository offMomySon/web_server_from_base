package domain;

import config.ConfigManager;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Objects;

// Todo]
// Path 만 인자로 주어지면 DownloadTarget 을 어디서나 생성할 수 있다.
// RequestTarget 에서만 생성하도록 강제할수 있을까?
// 강제하는게 올바른 선택인가? - 지금은 RequestTarget 에서만 생성된다.

// DownloadTarget 을 지정한 이유는 Download 할떄 일반 String, RequestTarget 으로 아무렇게나 다운도르 접근가능한것을 막기 위해서이다.

// 자바 문법상 한계가있음. 접근제어자로만 제한 가능 default 제어자.
public class DownloadTarget {
    private final Path value;

    private DownloadTarget(Path path) {
        this.value = validate(path);
    }

    public static DownloadTarget create(Path path){
        return new DownloadTarget(path);
    }

    private Path validate(Path path){
        String downloadPath = ConfigManager.getInstance().getDownloadConfig().getDownloadPath();

        if(Objects.isNull(path)){
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }
        if(path.toString().matches(downloadPath + ".*")){
            throw new IllegalArgumentException("path 의 시작부분에 downloadPath 를 가지면 안됩니다.");
        }

        return Path.of(downloadPath,path.toString());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadTarget that = (DownloadTarget) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
