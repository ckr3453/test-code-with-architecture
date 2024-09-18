package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getByEmail_은_ACTIVE_상태의_유저를_찾아올_수_있다() {
        //given
        String email = "david3453@naver.com";

        //when
        UserEntity result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("ckr");
    }

    @Test
    void getByEmail_은_PENDING_상태의_유저를_찾아올_수_없다() {
        //given
        String email = "david3454@naver.com";

        //when
        //then
        assertThatThrownBy(() -> userService.getByEmail(email)).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void getById_은_ACTIVE_상태의_유저를_찾아올_수_있다() {
        //given
        //when
        UserEntity result = userService.getById(1);

        //then
        assertThat(result.getNickname()).isEqualTo("ckr");
    }

    @Test
    void getById_은_PENDING_상태의_유저를_찾아올_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
    }


}
