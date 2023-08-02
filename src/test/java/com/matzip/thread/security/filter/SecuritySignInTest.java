package com.matzip.thread.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SecurityTestConfiguration.SecurityTestController.class)
@Import(SecurityTestConfiguration.class)
public class SecuritySignInTest {

    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 요청 실패: HTTP Method")
    void login_failure_http_method() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Supports only POST and application/json"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: Content Type")
    void login_failure_content_type() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Supports only POST and application/json"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("가입 되지 않은 유저 로그인 시도")
    void login_failure_username() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("anonymous", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username null")
    void login_failure_username_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest(null, "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: wrong password")
    void login_failure_password() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234_fail"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username empty")
    void login_failure_username_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username blank")
    void login_failure_username_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest(" ", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password null")
    void login_failure_password_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", null));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password empty")
    void login_failure_password_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", ""));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password blank")
    void login_failure_password_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", " "));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("non-existent user or a wrong password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 성공")
    void login_success() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
