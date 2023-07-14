package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.common.exception.NullArgumentException;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RoleOutPort {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<RoleEntity> findByRole(Role role) {
        if (Objects.isNull(role)) throw new NullArgumentException(Role.class.toString());

        return roleJpaRepository.findByRole(role).map(RoleJpaEntity::toEntity);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleJpaRepository.findAll().stream().map(RoleJpaEntity::toEntity).toList();
    }

    @Override
    public void save(RoleEntity roleEntity) {
        if (Objects.isNull(roleEntity)) throw new NullArgumentException(RoleEntity.class.toString());

        RoleJpaEntity parentRoleJpaEntity = null;
        if (Objects.nonNull(roleEntity.getParent())) {
            parentRoleJpaEntity = roleJpaRepository.findByRole(roleEntity.getParent())
                    .orElseThrow(() -> new NotFoundDataException(roleEntity.getParent().name()));
        }

        RoleJpaEntity savedRoleJpaEntity = roleJpaRepository.save(RoleJpaEntity.from(roleEntity, parentRoleJpaEntity));

        List<RoleJpaEntity> children = roleJpaRepository.findInRoles(roleEntity.getChildren());

        if (roleEntity.getChildren().size() != 0 && children.size() == 0) {
            throw new NotFoundDataException(roleEntity.getChildren().toString());
        }

        savedRoleJpaEntity.setChildren(children);
    }

    @Override
    public void update(Role role, RoleEntity roleEntity) {
        RoleJpaEntity findRoleJpaEntity = roleJpaRepository.findByRole(role)
                .orElseThrow(() -> new NotFoundDataException(role.name()));

        findRoleJpaEntity.setRole(roleEntity.getRole());
        findRoleJpaEntity.setDescription(roleEntity.getDescription());

        if (Objects.isNull(roleEntity.getParent())) {
            findRoleJpaEntity.setParent(null);
        } else {
            RoleJpaEntity parent = roleJpaRepository.findByRole(roleEntity.getParent())
                    .orElseThrow(() -> new NotFoundDataException(roleEntity.getParent().name()));
            findRoleJpaEntity.setParent(parent);
        }

        findRoleJpaEntity.setChildren(
                roleJpaRepository.findInRoles(roleEntity.getChildren())
        );
    }
}
