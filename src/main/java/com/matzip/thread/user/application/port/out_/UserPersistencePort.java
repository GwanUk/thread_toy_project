package com.matzip.thread.user.application.port.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

    Optional<UserEntity> findByUsername(String username);

    void save(UserEntity userEntity, Role role);

    List<UserEntity> findAll();
}
