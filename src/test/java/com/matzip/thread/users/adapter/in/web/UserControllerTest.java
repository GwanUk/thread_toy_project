package com.matzip.thread.users.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.thread.common.factorybean.PasswordEncoderFactoryBean;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(PasswordEncoderFactoryBean.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserUseCase userUseCase;

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
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        BDDMockito.then(userUseCase).should(Mockito.times(1)).signUp(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();
        BDDAssertions.then(userArgumentCaptorValue.getUsername()).isEqualTo("user");
        BDDAssertions.then(userArgumentCaptorValue.getNickname()).isEqualTo("kim");
        BDDAssertions.then(passwordEncoder.matches("1234", userArgumentCaptorValue.getPassword())).isTrue();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
                .andDo(MockMvcResultHandlers.print());
    }
}