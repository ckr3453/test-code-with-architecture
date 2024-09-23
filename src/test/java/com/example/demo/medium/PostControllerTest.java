package com.example.demo.medium;

import com.example.demo.post.domain.PostUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.example.demo.controller
 * fileName    : PostControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    // 테스트 메소드 시작전에 호출
    @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
    // 테스트 메소드 종료후에 호출
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_존재하는_게시글_정보를_가져올_수_있다() throws Exception {
        //given
        Long postId = 1L;

        //when
        ResultActions perform = mockMvc.perform(get("/api/posts/{postId}", postId));

        //then
        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.content").value("hello, world!"))
            .andExpect(jsonPath("$.createdAt").value(1678530673958L))
            .andExpect(jsonPath("$.modifiedAt").value(1678530673958L))
            .andExpect(jsonPath("$.writer.id").value(1))
            .andExpect(jsonPath("$.writer.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.writer.nickname").value("ckr"))
            .andExpect(jsonPath("$.writer.status").value("ACTIVE"))
            .andExpect(jsonPath("$.writer.lastLoginAt").value(0));
    }

    @Test
    void 사용자는_존재하지_않는_게시글_정보를_가져올_수_없다() throws Exception {
        //given
        Long wrongPostId = 12345L;

        //when
        ResultActions perform = mockMvc.perform(get("/api/posts/{postId}", wrongPostId));

        //then
        perform
            .andExpect(status().isNotFound())
            .andExpect(content().string("Posts에서 ID 12345를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_존재하는_게시글을_수정할_수_있다() throws Exception {
        //given
        Long postId = 1L;
        String newContent = "hello, test";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        //when
        ResultActions perform = mockMvc.perform(put("/api/posts/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postUpdate)));

        //then
        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.content").value(newContent))
            .andExpect(jsonPath("$.createdAt").value(1678530673958L))
            .andExpect(jsonPath("$.modifiedAt").isNumber())
            .andExpect(jsonPath("$.writer.id").value(1))
            .andExpect(jsonPath("$.writer.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.writer.nickname").value("ckr"))
            .andExpect(jsonPath("$.writer.status").value("ACTIVE"))
            .andExpect(jsonPath("$.writer.lastLoginAt").value(0));
    }

    @Test
    void 사용자는_존재하지_않는_게시글을_수정할_수_없다() throws Exception {
        //given
        Long wrongPostId = 12345L;
        String newContent = "hello, test code";
        PostUpdate postUpdate = PostUpdate.builder()
            .content(newContent)
            .build();

        //when
        ResultActions perform = mockMvc.perform(put("/api/posts/{postId}", wrongPostId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postUpdate)));

        //then
        perform
            .andExpect(status().isNotFound())
            .andExpect(content().string("Posts에서 ID 12345를 찾을 수 없습니다."));
    }

}
