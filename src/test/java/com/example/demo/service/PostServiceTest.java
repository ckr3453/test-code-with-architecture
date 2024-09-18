package com.example.demo.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.post.service.PostService;
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
    void postCreateDto_를_이용하여_post_를_생성할_수_있다() {
        //given
        String newContent = "hi";
        UserEntity writer = userService.getById(1);
        PostCreate postCreate = PostCreate.builder()
            .writerId(writer.getId())
            .content(newContent)
            .build();

        //when
        PostEntity result = postService.create(postCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(newContent);
        assertThat(result.getCreatedAt()).isPositive();
    }

    @Test
    void postUpdateDto_를_이용하여_post_를_수정할_수_있다() {
        //given
        UserEntity writer = userService.getById(1);
        String updateContent = "hi!!!";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(updateContent)
            .build();

        //when
        PostEntity result = postService.update(writer.getId(), postUpdate);

        //then
        assertThat(result.getContent()).isEqualTo(updateContent);
        assertThat(result.getModifiedAt()).isPositive();
    }

    @Test
    void getById_로_존재하는_post_를_찾아올_수_있다() {
        //given
        //when
        PostEntity result = postService.getById(1);

        //then
        assertThat(result.getContent()).isEqualTo("hello, world!");
        assertThat(result.getWriter().getEmail()).isEqualTo("david3453@naver.com");
    }

    @Test
    void getById_로_존재하지_않는_post_는_찾아올_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> postService.getById(3)).isInstanceOf(ResourceNotFoundException.class);
    }


}
