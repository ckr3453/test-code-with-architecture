package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : com.example.demo.controller
 * fileName    : PostControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class PostControllerTest {

    @Test
    void 사용자는_특정_게시글_정보를_가져올_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
            .build();
        long writerId = 1L;
        String writerEmail = "test@test.com";
        String writerNickname = "test";
        String writerAddress = "Incheon";
        long writerLastLoginAt = 123L;
        testContainer.userRepository.save(User.builder()
            .id(writerId)
            .email(writerEmail)
            .nickname(writerNickname)
            .address(writerAddress)
            .certificationCode("123123-12313-123123-123")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(writerLastLoginAt)
            .build());

        long postId = 1L;
        long createdAt = 123L;
        long modifiedAt = 123L;
        String content = "hello test?";
        testContainer.postRepository.save(Post.builder()
            .id(postId)
            .content(content)
            .createdAt(createdAt)
            .modifiedAt(modifiedAt)
            .writer(testContainer.userRepository.getById(1L))
            .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(postId);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getContent()).isEqualTo(content);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(modifiedAt);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(writerId);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo(writerEmail);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo(writerNickname);
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(writerLastLoginAt);
    }

    @Test
    void 사용자는_존재하지_않는_게시글_정보를_가져올_수_없다() {
        //given
        long wrongPostId = 12345L;
        TestContainer testContainer = TestContainer.builder()
            .build();

        //when
        //then
        assertThatThrownBy(() -> testContainer.postController.getPostById(wrongPostId))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시글을_수정할_수_있다() {
        //given
        long postModifiedAt = 11112334L;
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(new TestClockHolder(postModifiedAt))
            .build();
        long writerId = 1L;
        String writerEmail = "test@test.com";
        String writerNickname = "test";
        String writerAddress = "Incheon";
        long writerLastLoginAt = 123L;
        testContainer.userRepository.save(User.builder()
            .id(writerId)
            .email(writerEmail)
            .nickname(writerNickname)
            .address(writerAddress)
            .certificationCode("123123-12313-123123-123")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(writerLastLoginAt)
            .build());

        long postId = 1L;
        long postCreatedAt = 123L;
        String content = "hello test?";
        testContainer.postRepository.save(Post.builder()
            .id(postId)
            .content(content)
            .createdAt(postCreatedAt)
            .modifiedAt(postModifiedAt)
            .writer(testContainer.userRepository.getById(1L))
            .build());

        String newContent = "hello, test!!!";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(postId, postUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(postId);
        assertThat(result.getBody().getContent()).isEqualTo(newContent);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(postCreatedAt);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(postModifiedAt);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(writerId);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo(writerNickname);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo(writerEmail);
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(writerLastLoginAt);
    }

}
