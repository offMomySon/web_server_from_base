package response.messageFactory;

import domain.RequestTarget;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictedExtensionMessageFactoryTest {

    // [jihun]
    // config 처럼 실제 파일과 연관되어있는 객체의 테스트는 어떻게?..
    // 실제 config 를 읽어와야 어떤 확장자를 제한할지 알 수 있다.

    // [jihun]
    // 얘보다 downloadConfig test 를 작성해야 할 것 같은데.
    // 왜냐하면 restrictedExtensionMessageFactory 는 downloadConfig 의 기능을 빌려와 사용하는 동작을 함.

    // [jihun]
    // download config 는 데이터만 던져주고, 기능을 없애는게 좋을거 같은데.
    // 코드가 파편화 되더라도?
    @Test
    @DisplayName("제한되어있는 파일 확장자는 True 가 반환됩니다.")
    void isSupported() {
        //given
        RequestTarget requestTarget = RequestTarget.create("test.jpg");

        RestrictedExtensionMessageFactory restrictedExtensionMessageFactory = new RestrictedExtensionMessageFactory();

        //when
        boolean actual = restrictedExtensionMessageFactory.isSupported(requestTarget);

        //then
        Assertions.assertThat(actual).isTrue();
    }
}