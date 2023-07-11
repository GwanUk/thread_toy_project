package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RoleOutPort {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public RoleEntity findByRole(Role role) {
        return roleJpaRepository.findByRole(role).toEntity();
    }

    @Override
    public void save(RoleEntity roleEntity) {
        roleJpaRepository.save(RoleJpaEntity.fromEntity(roleEntity));
    }
}
