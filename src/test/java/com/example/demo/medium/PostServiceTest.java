package com.example.demo.medium;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
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
 * fileName    : PostServiceTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    // 테스트 메소드 시작전에 호출
    @Sql(value = "/sql/post-service-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
    // 테스트 메소드 종료후에 호출
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
public class PostServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Test
    void postFromDto_를_이용하여_post_를_생성할_수_있다() {
        //given
        long writerId = 1L;
        String newContent = "hi";
        User writer = userService.getById(writerId);
        PostCreate postCreate = PostCreate.builder()
            .writerId(writer.getId())
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
        User writer = userService.getById(writerId);
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        //when
        Post result = postService.update(writer.getId(), postUpdate);

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
