package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.post.controller.response
 * fileName    : PostResponseTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class PostResponseTest {

    @Test
    void Post_로_응답을_생성할_수_있다() {
        //given
        long writerId = 1L;
        String writerEmail = "test@test.com";
        String writerNickname = "test";
        String writerAddress = "Busan";
        String writerCertificationCode = "123123-123-123-123123123";
        String newContent = "hello!";

        User writer = User.builder()
            .id(writerId)
            .email(writerEmail)
            .nickname(writerNickname)
            .address(writerAddress)
            .status(UserStatus.ACTIVE)
            .certificationCode(writerCertificationCode)
            .build();
        Post post = Post.builder()
            .content(newContent)
            .writer(writer)
            .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo(newContent);
        assertThat(postResponse.getWriter().getId()).isEqualTo(writerId);
        assertThat(postResponse.getWriter().getEmail()).isEqualTo(writerEmail);
        assertThat(postResponse.getWriter().getNickname()).isEqualTo(writerNickname);
    }
}
