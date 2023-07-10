package com.matzip.thread.users.adapter.out_.persistence;

import com.matzip.thread.users.application.port.out_.UserGateWay;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class UsersRepository implements UserGateWay {

    private final UsersJpaRepository usersJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return usersJpaRepository.findByUsername(username).map(UsersJpaEntity::toDomainEntity);
    }

    @Override
    public void save(User user) {
        usersJpaRepository.save(UsersJpaEntity.fromDomainEntity(user));
    }
}
