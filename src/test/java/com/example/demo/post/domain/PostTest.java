package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : com.example.demo.post.domain
 * fileName    : PostTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class PostTest {

    @Test
    void PostCreate_로_게시물을_만들_수_있다() {
        // given
        long writerId = 1L;
        String writerEmail = "test@test.com";
        String writerNickname = "test";
        String writerAddress = "Busan";
        String writerCertificationCode = "123123-123-123-123123123";
        String newContent = "hello. test";

        PostCreate postCreate = PostCreate.builder()
            .writerId(writerId)
            .content(newContent)
            .build();
        User writer = User.builder()
            .id(writerId)
            .email(writerEmail)
            .nickname(writerNickname)
            .address(writerAddress)
            .status(UserStatus.ACTIVE)
            .certificationCode(writerCertificationCode)
            .build();

        // when
        long createdAt = 123456789L;
        Post post = Post.from(writer, postCreate, new TestClockHolder(createdAt));

        // then
        assertThat(post.getContent()).isEqualTo(newContent);
        assertThat(post.getCreatedAt()).isEqualTo(createdAt);
        assertThat(post.getWriter().getEmail()).isEqualTo(writerEmail);
        assertThat(post.getWriter().getNickname()).isEqualTo(writerNickname);
        assertThat(post.getWriter().getAddress()).isEqualTo(writerAddress);
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo(writerCertificationCode);
    }

    @Test
    void PostUpdate_로_게시물을_수정할_수_있다() {
        // given
        long writerId = 1L;
        String writerEmail = "test@test.com";
        String writerNickname = "test";
        String writerAddress = "Busan";
        String writerCertificationCode = "123123-123-123-123123123";
        String oldContent = "hello. test";

        User writer = User.builder()
            .id(writerId)
            .email(writerEmail)
            .nickname(writerNickname)
            .address(writerAddress)
            .status(UserStatus.ACTIVE)
            .certificationCode(writerCertificationCode)
            .build();

        Post post = Post.builder()
            .id(1L)
            .content(oldContent)
            .createdAt(1L)
            .modifiedAt(0L)
            .writer(writer)
            .build();

        String newContent = "hello. test??";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        // when
        long modifiedAt = 123456789L;
        Post updatedPost = post.update(postUpdate, new TestClockHolder(modifiedAt));

        // then
        assertThat(updatedPost.getContent()).isEqualTo(newContent);
        assertThat(updatedPost.getModifiedAt()).isEqualTo(modifiedAt);
    }
}
