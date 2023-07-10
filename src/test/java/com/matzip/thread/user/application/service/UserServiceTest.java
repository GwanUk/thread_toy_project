package com.matzip.thread.user.application.service;

import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.User;
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
    private UserOutPort userOutPort;

    @BeforeEach
    void init() {
        userService = new UserService(userOutPort);
    }

    @Test
    @DisplayName("username 으로 회원 단일 조회 서비스 성공")
    void findByUsername() {
        // given
        Optional<User> user = Optional.of(new User("user", "kim", "1234", Role.ROLE_USER));
        BDDMockito.given(userOutPort.findByUsername(Mockito.anyString())).willReturn(user);

        // when
        User findUser = userService.findByUsername("user").orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // then
        BDDAssertions.then(findUser.getUsername()).isEqualTo("user");
        BDDAssertions.then(findUser.getNickname()).isEqualTo("kim");
        BDDAssertions.then(findUser.getPassword()).isEqualTo("1234");
        BDDAssertions.then(findUser.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    @DisplayName("회원가입 서비스 성공")
    void singUp() {
        // given
        User user = new User("user", "kim", "1234", Role.ROLE_USER);

        // when
        userService.signUp(user);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        BDDMockito.then(userOutPort).should(Mockito.times(1)).save(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();
        BDDAssertions.then(userArgumentCaptorValue.getUsername()).isEqualTo("user");
        BDDAssertions.then(userArgumentCaptorValue.getNickname()).isEqualTo("kim");
        BDDAssertions.then(userArgumentCaptorValue.getPassword()).isEqualTo("1234");
        BDDAssertions.then(userArgumentCaptorValue.getRole()).isEqualTo(Role.ROLE_USER);
    }
}