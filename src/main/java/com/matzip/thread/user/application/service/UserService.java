package com.matzip.thread.user.application.service;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.in.UserWebPort;
import com.matzip.thread.user.application.port.out_.UserPersistencePort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserService implements UserWebPort {

    private final UserPersistencePort userPersistencePort;

    @Override
    public List<UserEntity> findAll() {
        return userPersistencePort.findAll();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userPersistencePort.findByUsername(username);
    }

    @Override
    @Transactional
    public void signUp(UserEntity userEntity, Role role) {
        userPersistencePort.save(userEntity, role);
    }
}
