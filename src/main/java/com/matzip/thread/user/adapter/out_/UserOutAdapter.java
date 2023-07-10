package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UserOutAdapter implements UserOutPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserJpaEntity::toDomainEntity);
    }

    @Override
    public void save(UserEntity userEntity) {
        userJpaRepository.save(UserJpaEntity.fromDomainEntity(userEntity));
    }
}
