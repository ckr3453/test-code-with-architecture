package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : com.example.demo.service
 * fileName    : UserServiceTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

class UserServiceTest {

    private UserService userService;
    private long testLastLoginAt;
    private String testCertificationCode;

    @BeforeEach
    void setUp() {
        this.testLastLoginAt = 123456789L;
        this.testCertificationCode = "123123-123123-123-123-123123";

        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserService.builder()
            .certificationService(new CertificationService(new FakeMailSender()))
            .clockHolder(new TestClockHolder(this.testLastLoginAt))
            .uuidHolder(new TestUuidHolder(this.testCertificationCode))
            .userRepository(fakeUserRepository)
            .build();

        fakeUserRepository.save(User.builder()
            .id(1L)
            .email("david3453@naver.com")
            .nickname("ckr")
            .address("Seoul, Gunja")
            .certificationCode("123123-12313-123123-123")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build());

        fakeUserRepository.save(User.builder()
            .id(2L)
            .email("david3454@naver.com")
            .nickname("ckr2")
            .address("Incheon")
            .certificationCode("123123-123123-123-123-123123123")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());
    }

    @Test
    void getByEmail_은_ACTIVE_상태의_user_를_찾아올_수_있다() {
        //given
        String activeUserEmail = "david3453@naver.com";
        String activeUserNickname = "ckr";

        //when
        User result = userService.getByEmail(activeUserEmail);

        //then
        assertThat(result.getNickname()).isEqualTo(activeUserNickname);
    }

    @Test
    void getByEmail_은_PENDING_상태의_user_를_찾아올_수_없다() {
        //given
        String pendingUserEmail = "david3454@naver.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(pendingUserEmail)).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById_은_ACTIVE_상태의_user_를_찾아올_수_있다() {
        //given
        long activeUserId = 1L;
        String activeUserNickname = "ckr";

        //when
        User result = userService.getById(activeUserId);

        //then
        assertThat(result.getNickname()).isEqualTo(activeUserNickname);
    }

    @Test
    void getById_은_PENDING_상태의_user_를_찾아올_수_없다() {
        //given
        long pendingUserId = 2L;

        //when
        //then
        assertThatThrownBy(() -> userService.getById(pendingUserId)).isInstanceOf(ResourceNotFoundException.class);
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

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo(testCertificationCode);
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
        userService.update(activeUserId, userUpdate);

        //then
        User result = userService.getById(activeUserId);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo(newAddress);
        assertThat(result.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void user_가_로그인_하면_마지막_로그인_시간이_수정된다() {
        //given
        long activeUserId = 1L;

        //when
        userService.login(activeUserId);

        //then
        User result = userService.getById(activeUserId);
        assertThat(result.getLastLoginAt()).isEqualTo(testLastLoginAt);
    }

    @Test
    void PENDING_상태의_user_는_인증_코드로_ACTIVE_상태로_변경할_수_있다() {
        //given
        long pendingUserId = 2L;
        String pendingUserCertificationCode = "123123-123123-123-123-123123123";

        //when
        userService.verifyEmail(pendingUserId, pendingUserCertificationCode);

        //then
        User result = userService.getById(pendingUserId);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_user_는_잘못된_인증_코드로_ACTIVE_상태로_변경할_수_없다() {
        //given
        long pendingUserId = 2L;
        String wrongCeritificationCode = "123123-123123-123-123-123123123-223";

        //when
        //then
        assertThatThrownBy(() -> userService.verifyEmail(pendingUserId, wrongCeritificationCode))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}

