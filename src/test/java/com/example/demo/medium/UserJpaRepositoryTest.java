package com.example.demo.medium;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.repository
 * fileName    : UserRepositoryTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        //given
        long activeUserId = 1L;

        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(activeUserId, UserStatus.ACTIVE);

        //then
        assertThat(result).isPresent();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        long activeUserId = 1L;

        //when
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(activeUserId, UserStatus.PENDING);

        //then
        assertThat(result).isNotPresent();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        //given
        String activeUserEmail = "david3453@naver.com";

        //when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus(activeUserEmail, UserStatus.ACTIVE);

        //then
        assertThat(result).isPresent();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        String wrongEmail = "test@test.com";

        //when
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus(wrongEmail, UserStatus.PENDING);

        //then
        assertThat(result).isNotPresent();
    }

}
