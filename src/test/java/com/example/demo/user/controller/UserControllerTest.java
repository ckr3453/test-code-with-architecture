package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : com.example.demo.controller
 * fileName    : UserControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

class UserControllerTest {


    @Test
    void 사용자는_특정_유저의_개인정보를_제외한_정보를_전달_받을_수_있다() {
        //given
        long activeUserId = 1L;
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository
            .save(User.builder()
                .id(activeUserId)
                .email("david3453@naver.com")
                .nickname("ckr")
                .address("Seoul, Gunja")
                .certificationCode("123123-123123-123-123-123123123")
                .status(UserStatus.ACTIVE)
                .build());

        //when
        ResponseEntity<UserResponse> result = testContainer.userController
            .getById(activeUserId);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(activeUserId);
        assertThat(result.getBody().getEmail()).isEqualTo("david3453@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("ckr");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_API_를_호출_할_경우_404_응답을_받는다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();

        //when
        //then
        long wrongUserId = 23L;
        assertThatThrownBy(() -> testContainer.userController.getById(wrongUserId))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_정상적인_인증_코드로_비활성화_계정을_활성화_할_수_있다() {
        //given
        long pendingUserId = 2L;
        String pendingUserCertificationCode = "123123-123123-123-123-123123";
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository
            .save(User.builder()
                .id(pendingUserId)
                .email("david3453@naver.com")
                .nickname("ckr")
                .address("Seoul")
                .certificationCode(pendingUserCertificationCode)
                .status(UserStatus.PENDING)
                .build());

        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(pendingUserId, pendingUserCertificationCode);

        //then
        User user = testContainer.userRepository.getById(pendingUserId);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_비정상적인_인증_코드로_비활성화_계정을_활성화_할_수_없다() {
        //given
        long pendingUserId = 2L;
        String pendingUserCertificationCode = "123123-123123-123-123-123123";
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository
            .save(User.builder()
                .id(pendingUserId)
                .email("david3453@naver.com")
                .nickname("ckr")
                .address("Seoul")
                .certificationCode(pendingUserCertificationCode)
                .status(UserStatus.PENDING)
                .build());

        //when
        //then
        assertThatThrownBy(() -> {
            String wrongCertificationCode = "123123";
            testContainer.userController.verifyEmail(pendingUserId, wrongCertificationCode);
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        //given
        String activeUserEmail = "david3453@naver.com";
        long lastLoginAt = 123456789L;
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(new TestClockHolder(lastLoginAt))
            .build();
        testContainer.userRepository
            .save(User.builder()
                .id(1L)
                .email(activeUserEmail)
                .nickname("ckr")
                .address("Seoul")
                .certificationCode("123124-123141-123123")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .build());

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo(activeUserEmail);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo(activeUserEmail);
        assertThat(result.getBody().getNickname()).isEqualTo("ckr");
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(lastLoginAt);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        String activeUserEmail = "david3453@naver.com";
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository
            .save(User.builder()
                .id(1L)
                .email(activeUserEmail)
                .nickname("ckr")
                .address("Seoul")
                .certificationCode("123124-123141-123123")
                .status(UserStatus.ACTIVE)
                .build());

        String newNickname = "ckr-update";
        String newAddress = "Incheon";
        UserUpdate userUpdate = UserUpdate.builder()
            .nickname(newNickname)
            .address(newAddress)
            .build();

        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
            .updateMyInfo(activeUserEmail, userUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo(activeUserEmail);
        assertThat(result.getBody().getNickname()).isEqualTo(newNickname);
        assertThat(result.getBody().getAddress()).isEqualTo(newAddress);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
