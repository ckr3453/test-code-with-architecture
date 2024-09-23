package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : com.example.demo.service
 * fileName    : PostServiceTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

public class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void setUp() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.postService = PostService.builder()
            .postRepository(fakePostRepository)
            .userRepository(fakeUserRepository)
            .clockHolder(new TestClockHolder(123456789L))
            .build();

        User writer = User.builder()
            .id(1L)
            .email("david3453@naver.com")
            .nickname("ckr")
            .address("Seoul, Gunja")
            .certificationCode("123123-123123-123-123-123123123")
            .status(UserStatus.ACTIVE)
            .lastLoginAt(0L)
            .build();

        fakeUserRepository.save(writer);
        fakePostRepository.save(Post.builder()
            .id(1L)
            .content("hello, world!")
            .createdAt(1678530673958L)
            .modifiedAt(1678530673958L)
            .writer(writer)
            .build());
        fakePostRepository.save(Post.builder()
            .id(2L)
            .content("hello, test world?!!")
            .createdAt(1678530673999L)
            .modifiedAt(1678530673999L)
            .writer(writer)
            .build());

        fakeUserRepository.save(User.builder()
            .id(2L)
            .email("david3454@naver.com")
            .nickname("ckr2")
            .address("Incheon")
            .certificationCode("123123-123123-123-123-123123123")
            .status(UserStatus.PENDING)
            .lastLoginAt(0L)
            .build());
    }

    @Test
    void postFromDto_를_이용하여_post_를_생성할_수_있다() {
        //given
        long writerId = 1L;
        String newContent = "hi";
        PostCreate postCreate = PostCreate.builder()
            .writerId(writerId)
            .content(newContent)
            .build();

        //when
        Post result = postService.create(postCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(newContent);
        assertThat(result.getCreatedAt()).isPositive();
    }

    @Test
    void postUpdateDto_를_이용하여_post_를_수정할_수_있다() {
        //given
        long writerId = 1L;
        String newContent = "hi!!!";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        //when
        Post result = postService.update(writerId, postUpdate);

        //then
        assertThat(result.getContent()).isEqualTo(newContent);
        assertThat(result.getModifiedAt()).isPositive();
    }

    @Test
    void getById_로_존재하는_post_를_찾아올_수_있다() {
        //given
        long postId = 1L;

        //when
        Post result = postService.getById(postId);

        //then
        assertThat(result.getContent()).isEqualTo("hello, world!");
        assertThat(result.getWriter().getEmail()).isEqualTo("david3453@naver.com");
    }

    @Test
    void getById_로_존재하지_않는_post_는_찾아올_수_없다() {
        //given
        long wrongPostId = 12345L;
        //when
        //then
        assertThatThrownBy(() -> postService.getById(wrongPostId)).isInstanceOf(ResourceNotFoundException.class);
    }


}
