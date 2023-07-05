package com.matzip.thread.users.adapter.out.persistence;

import com.matzip.thread.users.application.port.out.UserGateWay;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class UserRepository implements UserGateWay {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(long id) {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public long save(User user) {
        return 0;
    }

    @Override
    public long update(long id, User user) {
        return 0;
    }

    @Override
    public void delete(long id) {

    }
}
