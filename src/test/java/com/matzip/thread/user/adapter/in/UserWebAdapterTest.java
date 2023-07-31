package com.matzip.thread.user.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.in.UserWebPort;
import com.matzip.thread.user.domain.PasswordEncoderFactoryBean;
import com.matzip.thread.user.domain.UserEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    @DisplayName("회원 가입 요청 성공")
    void signUp() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("user", "kim", "1234"));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign_up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        BDDMockito.then(userWebPort).should(Mockito.times(1)).signUp(userArgumentCaptor.capture(), roleArgumentCaptor.capture());
        BDDAssertions.then(userArgumentCaptor.getValue().getUsername()).isEqualTo("user");
        BDDAssertions.then(userArgumentCaptor.getValue().getNickname()).isEqualTo("kim");
        BDDAssertions.then(passwordEncoder.matches("1234", userArgumentCaptor.getValue().getPassword())).isTrue();
        BDDAssertions.then(roleArgumentCaptor.getValue().equals(Role.ROLE_USER)).isTrue();
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username null")
    void signUp_fail_username_null() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest(null, "kim", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign_up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid argument value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username empty")
    void signUp_fail_username_empty() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("", "kim", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign_up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid argument value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 가입 요청 실패: username blank")
    void signUp_fail_username_blank() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest(" ", "kim", "1234"));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign_up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid argument value"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("username"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value(" "))
                .andDo(MockMvcResultHandlers.print());
    }
}