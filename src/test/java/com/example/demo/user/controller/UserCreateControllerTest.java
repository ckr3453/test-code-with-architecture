package com.example.demo.user.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.controller
 * fileName    : UserCreateControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원_가입된_사용자는_PENDING_상태이다() {
        //given
        String newEmail = "ckr3453@gmail.com";
        String newNickname = "cckr";
        String newAddress = "Suwon";
        UserCreate userCreate = UserCreate.builder()
            .email(newEmail)
            .nickname(newNickname)
            .address(newAddress)
            .build();

        String newCertificationCode = "123123-123-123123123-123";
        long createdAt = 1234L;
        TestContainer testContainer = TestContainer.builder()
            .uuidHolder(new TestUuidHolder(newCertificationCode))
            .clockHolder(new TestClockHolder(createdAt))
            .build();

        //when
        ResponseEntity<UserResponse> result = testContainer.userCreateController
            .create(userCreate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo(newEmail);
        assertThat(result.getBody().getNickname()).isEqualTo(newNickname);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNull();
    }

}
