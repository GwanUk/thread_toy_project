package com.matzip.thread.user.application.service;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.user.domain.UserEntity;
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
class UserEntityServiceTest {

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
        Optional<UserEntity> user = Optional.of(new UserEntity("user", "kim", "1234", new RoleEntity(Role.ROLE_USER, "유저 권한")));
        BDDMockito.given(userOutPort.findByUsername(Mockito.anyString())).willReturn(user);

        // when
        UserEntity findUserEntity = userService.findByUsername("user").orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // then
        BDDAssertions.then(findUserEntity.getUsername()).isEqualTo("user");
        BDDAssertions.then(findUserEntity.getNickname()).isEqualTo("kim");
        BDDAssertions.then(findUserEntity.getPassword()).isEqualTo("1234");
        BDDAssertions.then(findUserEntity.getRoleEntity().getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findUserEntity.getRoleEntity().getDescription()).isEqualTo("유저 권한");
    }

    @Test
    @DisplayName("회원가입 서비스 성공")
    void singUp() {
        // given
        UserEntity userEntity = new UserEntity("user", "kim", "1234", null);

        // when
        userService.signUp(userEntity, Role.ROLE_USER);

        // then
        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        BDDMockito.then(userOutPort).should(Mockito.times(1)).save(userArgumentCaptor.capture(), roleArgumentCaptor.capture());
        UserEntity userEntityArgumentCaptorValue = userArgumentCaptor.getValue();
        BDDAssertions.then(userEntityArgumentCaptorValue.getUsername()).isEqualTo("user");
        BDDAssertions.then(userEntityArgumentCaptorValue.getNickname()).isEqualTo("kim");
        BDDAssertions.then(userEntityArgumentCaptorValue.getPassword()).isEqualTo("1234");
        BDDAssertions.then(roleArgumentCaptor.getValue().equals(Role.ROLE_USER)).isTrue();
    }
}