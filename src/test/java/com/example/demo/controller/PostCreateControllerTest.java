package com.example.demo.controller;

import com.example.demo.post.domain.PostCreate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : com.example.demo.controller
 * fileName    : PostCreateControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/post-create-controller-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
class PostCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_게시글을_생성할_수_있다() throws Exception {
        //given
        long writerId = 3L;
        String createContent = "hi";
        PostCreate postCreate = PostCreate.builder()
            .writerId(writerId)
            .content(createContent)
            .build();

        //when
        ResultActions perform = mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postCreate)));

        //then
        perform
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.content").value(createContent))
            .andExpect(jsonPath("$.createdAt").isNumber())
            .andExpect(jsonPath("$.writer.id").value(writerId))
            .andExpect(jsonPath("$.writer.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.writer.nickname").value("ckr"))
            .andExpect(jsonPath("$.writer.status").value("ACTIVE"))
            .andExpect(jsonPath("$.writer.lastLoginAt").value(0));
    }

    @Test
    void 작성자가_존재하지_않으면_게시글을_생성할_수_없다() throws Exception {
        //given
        long wrongWriterId = 12345L;
        String createContent = "hi!!";
        PostCreate postCreate = PostCreate.builder()
            .writerId(wrongWriterId)
            .content(createContent)
            .build();

        //when
        ResultActions perform = mockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postCreate)));

        //then
        perform
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 12345를 찾을 수 없습니다."));
    }

}
