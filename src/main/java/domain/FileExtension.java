package domain;

import java.util.Objects;

public class FileExtension {
    private final String value;

    private String validate(String value){
        if(Objects.isNull(value)){
            throw new IllegalArgumentException("argument 가 null 이면 안됩니다.");
        }
        if(!value.startsWith(".")){
            throw new IllegalArgumentException("첫번째 문자는 '.' 이어야 합니다");
        }
        if(value.length() <= 1){
            throw new IllegalArgumentException("문자의 길이는 최소 2이상 이어야 합니다.");
        }
        if(value.contains(" ")){
            throw new IllegalArgumentException("확장자에 ' ' 가 포함되면 안됩니다.");
        }

        return value;
    }

    public FileExtension(String value) {
        this.value = validate(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileExtension that = (FileExtension) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "FileExtension{" +
                "value='" + value + '\'' +
                '}';
    }
}
