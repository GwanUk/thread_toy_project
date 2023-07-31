package com.matzip.thread.user.application.service;

import com.matzip.thread.user.application.port.out_.UserPersistencePort;
import com.matzip.thread.user.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.matzip.thread.role.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserPersistencePort userPersistencePort;

    @Test
    @DisplayName("username 으로 회원 단일 조회 서비스 성공")
    void findByUsername() {
        // given
        Optional<UserEntity> user = Optional.of(new UserEntity("user", "kim", "1234", ROLE_USER));
        given(userPersistencePort.findByUsername(Mockito.anyString())).willReturn(user);

        // when
        UserEntity findUserEntity = userService.findByUsername("user").orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // then
        assertThat(findUserEntity.getUsername()).isEqualTo("user");
        assertThat(findUserEntity.getNickname()).isEqualTo("kim");
        assertThat(findUserEntity.getPassword()).isEqualTo("1234");
        assertThat(findUserEntity.getRole()).isEqualTo(ROLE_USER);
    }

    @Test
    @DisplayName("회원가입 서비스 성공")
    void singUp() {
        // given
        UserEntity userEntity = new UserEntity("user", "kim", "1234", null);

        // when
        userService.signUp(userEntity);

        // then
        ArgumentCaptor<UserEntity> userArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        then(userPersistencePort).should(Mockito.times(1)).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo("user");
        assertThat(userArgumentCaptor.getValue().getNickname()).isEqualTo("kim");
        assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("회원가입시 권한은 유저 권한")
    void signUp_role_user() {
        // given
        UserEntity entity = new UserEntity("user01", "유저", "1234", null);

        // when
        userService.signUp(entity);

        // then
        ArgumentCaptor<UserEntity> ac = ArgumentCaptor.forClass(UserEntity.class);
        then(userPersistencePort).should().save(ac.capture());
        assertThat(ac.getValue().getUsername()).isEqualTo("user01");
        assertThat(ac.getValue().getNickname()).isEqualTo("유저");
        assertThat(ac.getValue().getPassword()).isEqualTo("1234");
        assertThat(ac.getValue().getRole()).isEqualTo(ROLE_USER);
    }
}