package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : com.example.demo.user.domain
 * fileName    : UserTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class UserTest {

    @Test
    void User_는_UserCreate_객체로_생성할_수_있다() {
        // given
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";

        UserCreate userCreate = UserCreate.builder()
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder(userCertificationCode));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(userEmail);
        assertThat(user.getNickname()).isEqualTo(userNickname);
        assertThat(user.getAddress()).isEqualTo(userAddress);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo(userCertificationCode);
    }

    @Test
    void User_는_UserUpdate_객체로_데이터를_수정할_수_있다() {
        // given
        long userId = 1L;
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";
        long lastLoginAt = 100L;

        User user = User.builder()
            .id(userId)
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .certificationCode(userCertificationCode)
            .lastLoginAt(lastLoginAt)
            .status(UserStatus.ACTIVE)
            .build();

        String newNickname = "test2";
        String newAddress = "Incheon";

        UserUpdate userUpdate = UserUpdate.builder()
            .nickname(newNickname)
            .address(newAddress)
            .build();

        // when
        User updateUser = user.update(userUpdate);

        // then
        assertThat(updateUser.getId()).isEqualTo(userId);
        assertThat(updateUser.getEmail()).isEqualTo(userEmail);
        assertThat(updateUser.getNickname()).isEqualTo(newNickname);
        assertThat(updateUser.getAddress()).isEqualTo(newAddress);
        assertThat(updateUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(updateUser.getCertificationCode()).isEqualTo(userCertificationCode);
        assertThat(updateUser.getLastLoginAt()).isEqualTo(lastLoginAt);
    }

    @Test
    void User_는_로그인을_할_수_있고_로그인_시_마지막_로그인_시간이_변경된다() {
        // given
        long userId = 1L;
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";
        long lastLoginAt = 100L;

        User user = User.builder()
            .id(userId)
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .certificationCode(userCertificationCode)
            .lastLoginAt(lastLoginAt)
            .status(UserStatus.ACTIVE)
            .build();

        // when
        lastLoginAt = 12345L;
        User loginedUser = user.login(new TestClockHolder(lastLoginAt));

        // then
        assertThat(loginedUser.getLastLoginAt()).isEqualTo(lastLoginAt);
    }

    @Test
    void User_는_유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        long userId = 1L;
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";
        long lastLoginAt = 100L;

        User user = User.builder()
            .id(userId)
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .certificationCode(userCertificationCode)
            .lastLoginAt(lastLoginAt)
            .status(UserStatus.PENDING)
            .build();

        // when
        User certificatedUser = user.certificate(userCertificationCode);

        // then
        assertThat(certificatedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void User_는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() {
        // given
        long userId = 1L;
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";
        long lastLoginAt = 100L;

        User user = User.builder()
            .id(userId)
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .certificationCode(userCertificationCode)
            .lastLoginAt(lastLoginAt)
            .status(UserStatus.PENDING)
            .build();

        // when
        // then
        String wrongCertificationCode = "1234";
        assertThatThrownBy(() -> user.certificate(wrongCertificationCode)).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
