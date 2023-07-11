package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RoleOutPort {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<RoleEntity> findByRole(Role role) {
        return roleJpaRepository.findByRole(role).map(RoleJpaEntity::toEntity);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleJpaRepository.findAll().stream().map(RoleJpaEntity::toEntity).toList();
    }

    @Override
    public void save(RoleEntity roleEntity) {
        roleJpaRepository.save(RoleJpaEntity.fromEntity(roleEntity));
    }
}
