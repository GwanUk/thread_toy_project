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

import java.util.Optional;

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
    @DisplayName("username 으로 회원 단일 조회 서비스 성공")
    void findByUsername() {
        // given
        Optional<User> user = Optional.of(new User("user", "kim", "1234", Role.USER));
        BDDMockito.given(userGateWay.findByUsername(Mockito.anyString())).willReturn(user);

        // when
        User findUser = userService.findByUsername("user").orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // then
        BDDAssertions.then(findUser.getUsername()).isEqualTo("user");
        BDDAssertions.then(findUser.getNickname()).isEqualTo("kim");
        BDDAssertions.then(findUser.getPassword()).isEqualTo("1234");
        BDDAssertions.then(findUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("회원가입 서비스 성공")
    void singUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("user", "kim", "1234");

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