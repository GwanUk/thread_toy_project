package com.matzip.thread.users.adapter.out.persistence;

import com.matzip.thread.users.application.port.out.UserEntity;
import com.matzip.thread.users.application.port.out.UserGateWay;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class UserRepository implements UserGateWay {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다.")).toDomainEntity();
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserEntity::toDomainEntity)
                .toList();
    }

    @Override
    public Long save(User user) {
        return userJpaRepository.save(UserEntity.formDomainEntity(user)).getId();
    }

    @Override
    public void update(long id, User user) {

    }

    @Override
    public void delete(long id) {

    }
}
