package com.example.demo.controller;

import com.example.demo.user.domain.UserUpdate;
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
 * fileName    : UserControllerTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    // 테스트 메소드 시작전에 호출
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = BEFORE_TEST_METHOD),
    // 테스트 메소드 종료후에 호출
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_개인정보를_제외한_정보를_전달_받을_수_있다() throws Exception {
        //given
        Long userId = 1L;

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}", userId));

        //then
        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.nickname").value("ckr"))
            .andExpect(jsonPath("$.address").doesNotExist())
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_API_를_호출_할_경우_404_응답을_받는다() throws Exception {
        //given
        Long wrongUserId = 12345L;

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}", wrongUserId));

        //then
        perform
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 12345를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_정상적인_인증_코드로_비활성화_계정을_활성화_할_수_있다() throws Exception {
        //given
        Long pendingUserId = 2L;
        String pendingUserCertificationCode = "123123-123123-123-123-123123";

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}/verify", pendingUserId)
            .queryParam("certificationCode", pendingUserCertificationCode));

        //then
        perform.andExpect(status().isFound());
    }

    @Test
    void 사용자는_비정상적인_인증_코드로_비활성화_계정을_활성화_할_수_없다() throws Exception {
        //given
        Long pendingUserId = 2L;
        String wrongCertificationCode = "12345";

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}/verify", pendingUserId)
            .queryParam("certificationCode", wrongCertificationCode));

        //then
        perform.andExpect(status().isForbidden());
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        //given
        String userEmail = "david3453@naver.com";

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/me")
            .header("EMAIL", userEmail));

        //then
        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.nickname").value("ckr"))
            .andExpect(jsonPath("$.address").value("Seoul, Gunja"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        String userEmail = "david3453@naver.com";
        String updateNickname = "ckr-update";
        String updateAddress = "Incheon";
        UserUpdate userUpdate = UserUpdate.builder()
            .nickname(updateNickname)
            .address(updateAddress)
            .build();

        //when
        ResultActions perform = mockMvc.perform(put("/api/users/me")
            .header("EMAIL", userEmail)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdate)));

        //then
        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("david3453@naver.com"))
            .andExpect(jsonPath("$.nickname").value(updateNickname))
            .andExpect(jsonPath("$.address").value(updateAddress))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
