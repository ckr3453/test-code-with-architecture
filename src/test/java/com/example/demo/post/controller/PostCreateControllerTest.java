package com.example.demo.post.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.controller
 * fileName    : PostCreateControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */


class PostCreateControllerTest {

    @Test
    void 사용자는_게시글을_생성할_수_있다() {
        //given
        long createdAt = 1111231231L;
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(new TestClockHolder(createdAt))
            .build();

        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("david3453@naver.com")
            .nickname("ckr")
            .address("Seoul, Gunja")
            .certificationCode("123123-12313-123123-123")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(123L)
            .build());

        String newContent = "hi";
        PostCreate postCreate = PostCreate.builder()
            .writerId(1L)
            .content(newContent)
            .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.create(postCreate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo(newContent);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("david3453@naver.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("ckr");
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(123L);
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
