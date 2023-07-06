package com.matzip.thread.users.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.security.configs.SecurityConfig;
import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @MockBean
    private  UserUseCase userUseCase;
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 요청 성공")
    void login_success() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new User("user", "kim", "1234", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("kim"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(Role.ROLE_USER.name()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 요청 실패: The user does not exist: user")
    void login_failure_username() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new User("user", "kim", "1234", Role.ROLE_USER));
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(null);

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
        String json = objectMapper.writeValueAsString(new User("user", "kim", "1234_fail", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
        String json = objectMapper.writeValueAsString(new User("user", "kim", "1234", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
        String json = objectMapper.writeValueAsString(new User("user", "kim", "1234", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
    @DisplayName("로그인 요청 실패: username empty")
    void login_failure_username_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new User("", "kim", "1234", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
        String json = objectMapper.writeValueAsString(new User(" ", "kim", "1234", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
        String json = objectMapper.writeValueAsString(new User("user", "kim", "", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
        String json = objectMapper.writeValueAsString(new User("user", "kim", " ", Role.ROLE_USER));
        User userEncoded = new User("user", "kim", passwordEncoder.encode("1234"), Role.ROLE_USER);
        BDDMockito.given(userUseCase.getByUsername(Mockito.anyString())).willReturn(userEncoded);

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
    @DisplayName("회원 가입 요청 성공")
    void signUp() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("user", "kim", "1234", Role.ROLE_USER));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<SignUpRequest> signUpRequestArgumentCaptor = ArgumentCaptor.forClass(SignUpRequest.class);
        BDDMockito.then(userUseCase).should(Mockito.times(1)).signUp(signUpRequestArgumentCaptor.capture());
        SignUpRequest signUpRequestArgumentCaptorValue = signUpRequestArgumentCaptor.getValue();
        BDDAssertions.then(signUpRequestArgumentCaptorValue.getUsername()).isEqualTo("user");
        BDDAssertions.then(signUpRequestArgumentCaptorValue.getNickname()).isEqualTo("kim");
        BDDAssertions.then(passwordEncoder.matches("1234", signUpRequestArgumentCaptorValue.getPassword())).isTrue();
        BDDAssertions.then(signUpRequestArgumentCaptorValue.getRole()).isEqualTo(Role.ROLE_USER);
    }
}