package com.example.demo.user.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.user.controller
 * fileName    : MyInfoControllerTest
 * author      : ckr
 * date        : 24. 11. 3.
 * description :
 */
public class MyInfoControllerTest {
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
        ResponseEntity<MyProfileResponse> result = testContainer.myInfoController.getByEmail(activeUserEmail);

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
        ResponseEntity<MyProfileResponse> result = testContainer.myInfoController
            .update(activeUserEmail, userUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo(activeUserEmail);
        assertThat(result.getBody().getNickname()).isEqualTo(newNickname);
        assertThat(result.getBody().getAddress()).isEqualTo(newAddress);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
