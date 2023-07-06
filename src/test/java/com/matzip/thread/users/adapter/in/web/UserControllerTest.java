package com.matzip.thread.users.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.matzip.thread.users.UserControllerTestContextConfiguration;
import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.Role;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
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
@Import(UserControllerTestContextConfiguration.class)
class UserControllerTest {

    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @MockBean
    private  UserUseCase userUseCase;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 요청 성공")
    void login() throws Exception {
        // given
        ObjectNode jsonNodes = objectMapper.createObjectNode()
                .put("username", "user")
                .put("nickname", "kim")
                .put("password", "1234")
                .put("role", "USER");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sing_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNodes.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
    }

    @Test
    @DisplayName("회원 가입 요청 성공")
    void signUp() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new SignUpRequest("user", "kim", "1234", Role.USER));

        BDDMockito.given(passwordEncoder.encode(ArgumentMatchers.any(CharSequence.class))).willReturn("1234");

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
        BDDAssertions.then(signUpRequestArgumentCaptorValue.getPassword()).isEqualTo("1234");
        BDDAssertions.then(signUpRequestArgumentCaptorValue.getRole()).isEqualTo(Role.USER);
    }
}