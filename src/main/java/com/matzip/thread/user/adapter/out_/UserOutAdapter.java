package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UserOutAdapter implements UserOutPort {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserJpaEntity::toEntity);
    }

    @Override
    public void save(UserEntity userEntity, Role role) {
        RoleJpaEntity roleJpaEntity = roleJpaRepository.findByRole(role);
        userJpaRepository.save(UserJpaEntity.fromEntity(userEntity, roleJpaEntity));
    }
}
