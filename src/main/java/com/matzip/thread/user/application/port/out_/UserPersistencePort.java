package com.matzip.thread.user.application.port.out_;

import com.matzip.thread.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

    List<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String username);

    void save(UserEntity userEntity);

    void update(String username, UserEntity userEntity);

    void delete(String username);
}
