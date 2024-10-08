package com.example.demo.medium;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * packageName : com.example.demo.service
 * fileName    : UserServiceTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    // 테스트 메소드 시작전에 호출
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
    // 테스트 메소드 종료후에 호출
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail_은_ACTIVE_상태의_user_를_찾아올_수_있다() {
        //given
        String activeUserEmail = "david3453@naver.com";
        String activeUserNickname = "ckr";

        //when
        User result = userServiceImpl.getByEmail(activeUserEmail);

        //then
        assertThat(result.getNickname()).isEqualTo(activeUserNickname);
    }

    @Test
    void getByEmail_은_PENDING_상태의_user_를_찾아올_수_없다() {
        //given
        String pendingUserEmail = "david3454@naver.com";

        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.getByEmail(pendingUserEmail)).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById_은_ACTIVE_상태의_user_를_찾아올_수_있다() {
        //given
        long activeUserId = 1L;
        String activeUserNickname = "ckr";

        //when
        User result = userServiceImpl.getById(activeUserId);

        //then
        assertThat(result.getNickname()).isEqualTo(activeUserNickname);
    }

    @Test
    void getById_은_PENDING_상태의_user_를_찾아올_수_없다() {
        //given
        long pendingUserId = 2L;

        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.getById(pendingUserId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_user_를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
            .email("david3453@kakao.com")
            .address("Seoul, Gangnam")
            .nickname("ckr3")
            .build();
        // (실제 인증과정 중 메일 보내는 행위를 mock을 통해 대체(아무것도 하지않음))
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userServiceImpl.create(userCreate);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // assertThat(result.getCertificationCode()).isEqualTo("??") // UUID여서 테스트 불가, 리팩토링 필요함.
    }

    @Test
    void userUpdateDto_를_이용하여_user_를_수정할_수_있다() {
        //given
        long activeUserId = 1L;
        String newAddress = "Busan";
        String newNickname = "ckr-busan";
        UserUpdate userUpdate = UserUpdate.builder()
            .address(newAddress)
            .nickname(newNickname)
            .build();

        //when
        userServiceImpl.update(activeUserId, userUpdate);

        //then
        User result = userServiceImpl.getById(activeUserId);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo(newAddress);
        assertThat(result.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void user_가_로그인_하면_마지막_로그인_시간이_수정된다() {
        //given
        long activeUserId = 1L;

        //when
        userServiceImpl.login(activeUserId);

        //then
        User result = userServiceImpl.getById(activeUserId);
        assertThat(result.getLastLoginAt()).isPositive();
    }

    @Test
    void PENDING_상태의_user_는_인증_코드로_ACTIVE_상태로_변경할_수_있다() {
        //given
        long pendingUserId = 2L;
        String pendingUserCertificationCode = "123123-123123-123-123-123123123";

        //when
        userServiceImpl.verifyEmail(pendingUserId, pendingUserCertificationCode);

        //then
        User result = userServiceImpl.getById(pendingUserId);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_user_는_잘못된_인증_코드로_ACTIVE_상태로_변경할_수_없다() {
        //given
        long pendingUserId = 2L;
        String wrongCeritificationCode = "123123-123123-123-123-123123123-223";

        //when
        //then
        assertThatThrownBy(() -> userServiceImpl.verifyEmail(pendingUserId, wrongCeritificationCode))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}

