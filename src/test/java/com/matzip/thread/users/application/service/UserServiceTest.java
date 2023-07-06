package com.matzip.thread.users.application.service;

import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.out.UserGateWay;
import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserGateWay userGateWay;

    @BeforeEach
    void init() {
        userService = new UserService(userGateWay);
    }

    @Test
    @DisplayName("회원가입 서비스 성공")
    void singUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("user", "kim", "1234", Role.USER);

        // when
        userService.signUp(signUpRequest);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        BDDMockito.then(userGateWay).should(Mockito.times(1)).save(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();
        BDDAssertions.then(userArgumentCaptorValue.getUsername()).isEqualTo("user");
        BDDAssertions.then(userArgumentCaptorValue.getNickname()).isEqualTo("kim");
        BDDAssertions.then(userArgumentCaptorValue.getPassword()).isEqualTo("1234");
        BDDAssertions.then(userArgumentCaptorValue.getRole()).isEqualTo(Role.USER);
    }
}