package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DownloadRate {
    private final int count;
    private final long period;

    // ㄹㅏ이브러리 지식. 추가 공부필요
//    @JsonDeserialize(using ={})

    @JsonCreator
    public DownloadRate(@JsonProperty("count") int count, @JsonProperty("period") long period) {
        this.count = count;
        this.period = period;
    }
}
