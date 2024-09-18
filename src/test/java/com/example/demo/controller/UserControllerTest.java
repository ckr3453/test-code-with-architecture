package com.example.demo.controller;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자는_특정_유저의_개인정보를_제외한_정보를_전달_받을_수_있다() throws Exception {
        //given
        UserEntity userEntity = userService.getById(1);

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}", userEntity.getId()));

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
    @Transactional
    void 사용자는_인증_코드로_비활성화_계정을_활성화_할_수_있다() throws Exception {
        //given
        UserEntity pendingUser = userRepository.findById(2L).get();

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/{userId}/verify", pendingUser.getId())
            .queryParam("certificationCode", pendingUser.getCertificationCode()));

        //then
        perform.andExpect(status().isFound());
        assertThat(pendingUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        //given
        UserEntity userEntity = userService.getById(1);

        //when
        ResultActions perform = mockMvc.perform(get("/api/users/me")
            .header("EMAIL", userEntity.getEmail()));

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
        UserEntity userEntity = userService.getById(1);
        String updateNickname = "ckr-update";
        String updateAddress = "Incheon";
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .nickname(updateNickname)
            .address(updateAddress)
            .build();

        //when
        ResultActions perform = mockMvc.perform(put("/api/users/me")
            .header("EMAIL", userEntity.getEmail())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userUpdateDto)));

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
