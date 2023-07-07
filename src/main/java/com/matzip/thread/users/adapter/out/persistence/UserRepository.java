package com.matzip.thread.users.adapter.out.persistence;

import com.matzip.thread.users.application.port.out.UserGateWay;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UserRepository implements UserGateWay {

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
