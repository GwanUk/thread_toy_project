package com.matzip.thread.user.adapter.out_.persistence;

import com.matzip.thread.user.application.port.out_.UserOutPort;
import com.matzip.thread.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UserOutAdapter implements UserOutPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserJpaEntity::toDomainEntity);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(UserJpaEntity.fromDomainEntity(user));
    }
}
