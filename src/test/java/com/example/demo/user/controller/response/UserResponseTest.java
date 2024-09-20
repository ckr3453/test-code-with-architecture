package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.user.controller.response
 * fileName    : UserResponseTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class UserResponseTest {

    @Test
    void User_로_응답을_생성할_수_있다() {
        // given
        long userId = 1L;
        String userEmail = "test@test.com";
        String userNickname = "test";
        String userAddress = "Busan";
        String userCertificationCode = "123123-123-123-123123123";

        User user = User.builder()
            .id(userId)
            .email(userEmail)
            .nickname(userNickname)
            .address(userAddress)
            .status(UserStatus.ACTIVE)
            .certificationCode(userCertificationCode)
            .build();

        // when
        UserResponse userResponse = UserResponse.from(user);

        // then
        assertThat(userResponse.getId()).isEqualTo(userId);
        assertThat(userResponse.getEmail()).isEqualTo(userEmail);
        assertThat(userResponse.getNickname()).isEqualTo(userNickname);
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
