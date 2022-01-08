package config.server.download;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import config.server.download.data.DownloadInfoAtIp;
import config.server.download.data.DownloadRate;
import domain.FileExtension;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

// Todo 1
// find 만을 위한 1급 컬렉션,
// 1. DownloadConfig 로 부터 find 만을 위한 객체를 생성 이란 생각을 하기 어렵다.
//
// 2. class 기능이 너무 적은거 같다. find.
//    config 에 모든 기능을 이동시키면 안되나?
//
// 3. Map 으로 꼭 주소와 객체를 매핑해야할까?.. Set 으로도 충분히 찾을수 있는데?
@Getter
public class DownloadInfoRepository {
    private final Map<String, DownloadInfoAtIp> values;
    private final DownloadInfoAtIp commonIp;

    private DownloadInfoRepository(@NonNull Map<String, DownloadInfoAtIp> values,
                                   @NonNull DownloadInfoAtIp commonIp) {
        this.values = values;
        this.commonIp = commonIp;
    }

    public static DownloadInfoRepository create(DownloadRate downloadRate,
                                                Set<FileExtension> restrictedFileExtension,
                                                Set<DownloadInfoAtIp> downloadInfoAtIps) {

        Map<String, DownloadInfoAtIp> newValues = downloadInfoAtIps.stream()
                .collect(Collectors.toMap(DownloadInfoAtIp::getHostAddress, Function.identity(), (it1, it2) -> it1));

        return new DownloadInfoRepository(newValues, DownloadInfoAtIp.allMatched(downloadRate, restrictedFileExtension));
    }

    public DownloadInfoAtIp find(@NonNull String ip) {
        return values.getOrDefault(ip, commonIp);
    }
}
