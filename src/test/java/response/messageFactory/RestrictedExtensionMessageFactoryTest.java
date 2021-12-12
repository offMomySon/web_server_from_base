package response.messageFactory;

import domain.RequestTarget;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictedExtensionMessageFactoryTest {
    
    // [jihun]
    // config 처럼 실제 파일과 연관되어있는 객체의 테스트는 어떻게?..
    // 얘보다 downloadConfig 의 test 의 test 를 작성해야 할 것 같은데. - 왜냐하면 실제 동작하는 메서드가 있음.
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