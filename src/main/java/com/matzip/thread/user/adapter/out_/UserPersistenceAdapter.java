package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.out_.UserPersistencePort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class UserPersistenceAdapter implements UserPersistencePort {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAllWithRole().stream().map(UserJpaEntity::toEntity).toList();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userJpaRepository.findByUsernameWithRole(username).map(UserJpaEntity::toEntity);
    }

    @Override
    public void save(UserEntity userEntity) {
        Role role = userEntity.getRole();
        RoleJpaEntity roleJpaEntity = roleJpaRepository.findByRole(role).orElseThrow(() -> new NotFoundDataException(role.name()));
        userJpaRepository.save(UserJpaEntity.from(userEntity, roleJpaEntity));
    }

    @Override
    public void update(String username, UserEntity userEntity) {
        UserJpaEntity userJpaEntity = userJpaRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new NotFoundDataException(username));

        Role role = userEntity.getRole();
        RoleJpaEntity roleJpaEntity = roleJpaRepository.findByRole(role).orElseThrow(() -> new NotFoundDataException(role.name()));

        userJpaEntity.update(userEntity, roleJpaEntity);
    }

    @Override
    public void delete(String username) {
        userJpaRepository.deleteByUsername(username);
    }
}
