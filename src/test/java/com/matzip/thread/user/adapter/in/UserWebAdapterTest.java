package com.matzip.thread.user.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.user.application.port.in.UserWebPort;
import com.matzip.thread.user.domain.PasswordEncoderFactoryBean;
import com.matzip.thread.user.domain.UserEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.matzip.thread.role.domain.Role.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserWebAdapter.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(PasswordEncoderFactoryBean.class)
class UserWebAdapterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserWebPort userWebPort;

    @Test
    @DisplayName("전체 조회")
    void findAll() throws Exception {
        // given
        given(userWebPort.findAll()).willReturn(List.of(new UserEntity("user01", "관리자", "1234", ROLE_ADMIN)));

        // when
        mockMvc.perform(get("/api/users")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user01"))
                .andExpect(jsonPath("$[0].nickname").value("관리자"))
                .andExpect(jsonPath("$[0].role").value(ROLE_ADMIN.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("단일 조회")
    void findByUsername() throws Exception {
        // given
        given(userWebPort.findByUsername(anyString())).willReturn(Optional.of(new UserEntity("user01", "관리자", "1234", ROLE_ADMIN)));

        // when
        mockMvc.perform(get("/api/users/{username}", "user01")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user01"))
                .andExpect(jsonPath("$.nickname").value("관리자"))
                .andExpect(jsonPath("$.role").value(ROLE_ADMIN.name()))
                .andDo(print());

        // then
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        then(userWebPort).should().findByUsername(ac.capture());
        assertThat(ac.getValue()).isEqualTo("user01");
    }

    @Test
    @DisplayName("회원 가입 요청 성공")
    void signUp() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("user", "kim", "1234"));

        // when
        mockMvc.perform(post("/api/users/sign_up")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        then(userWebPort).should(Mockito.times(1)).signUp(userArgumentCaptor.capture());
        BDDAssertions.then(userArgumentCaptor.getValue().getUsername()).isEqualTo("user");
        BDDAssertions.then(userArgumentCaptor.getValue().getNickname()).isEqualTo("kim");
        BDDAssertions.then(passwordEncoder.matches("1234", userArgumentCaptor.getValue().getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username null")
    void signUp_fail_username_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest(null, "kim", "1234"));

        // expected
        mockMvc.perform(post("/api/users/sign_up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username empty")
    void signUp_fail_username_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("", "kim", "1234"));

        // expected
        mockMvc.perform(post("/api/users/sign_up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username blank")
    void signUp_fail_username_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest(" ", "kim", "1234"));

        // expected
        mockMvc.perform(post("/api/users/sign_up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument value"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue").value(" "))
                .andDo(print());
    }
}