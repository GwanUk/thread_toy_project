package com.matzip.thread.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.ApplicationConfiguration;
import com.matzip.thread.security.configs.SecurityConfig;
import com.matzip.thread.users.application.port.in.SignInRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest
@Import(value = {SecurityConfig.class, ApplicationConfiguration.class})
public class SecurityTest {

    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private  UserUseCase userUseCase;

    @Test
    @DisplayName("로그인 요청 성공")
    void login_success() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: The user does not exist: user")
    void login_failure_username() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.empty());

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("The user does not exist: user"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: Invalid password")
    void login_failure_password() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234_fail"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid password"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: HTTP Method")
    void login_failure_http_method() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Supports only POST HTTP Method and application/json Content-Type"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: Content Type")
    void login_failure_content_type() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Supports only POST HTTP Method and application/json Content-Type"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username null")
    void login_failure_username_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest(null, "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username empty")
    void login_failure_username_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("", "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: username blank")
    void login_failure_username_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest(" ", "1234"));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password null")
    void login_failure_password_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", null));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password empty")
    void login_failure_password_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", ""));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: password blank")
    void login_failure_password_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignInRequest("user", " "));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.USER);
        BDDMockito.given(userUseCase.findByUsername(Mockito.anyString())).willReturn(Optional.of(userEncoded));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("username or Password is required value"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("요청 실패: 인증 되지 않는 사용자")
    void login_failure_unauthorized() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("UnAuthorized. Only authorized users have access"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("요청 실패: 인가 되지 않은 사용자")
    void login_failure_forbidden() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("Access denied. Authorization is required"))
                .andDo(MockMvcResultHandlers.print());
    }
}
