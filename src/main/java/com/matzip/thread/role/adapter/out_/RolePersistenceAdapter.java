package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.NullCheck;
import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.annotation.Validation;
import com.matzip.thread.common.exception.ApplicationConventionViolationException;
import com.matzip.thread.common.exception.NotFoundDataException;
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
    @Validation
    public Optional<RoleEntity> findByRole(Role role) {
        return roleJpaRepository.findByRole(role).map(RoleJpaEntity::toEntity);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleJpaRepository.findAll().stream().map(RoleJpaEntity::toEntity).toList();
    }

    @Override
    @Validation
    public void save(@NullCheck RoleEntity roleEntity) {
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
    @Validation
    public void update(Role role, @NullCheck RoleEntity roleEntity) {
        RoleJpaEntity findRoleJpaEntity = roleJpaRepository.findByRole(role)
                .orElseThrow(() -> new NotFoundDataException(role.name()));

        if (!role.equals(roleEntity.getRole()) &&
                roleJpaRepository.findByRole(roleEntity.getRole()).isPresent()) {
            throw new ApplicationConventionViolationException(roleEntity.getRole().name() + " that already exists");
        }

        findRoleJpaEntity.setRole(roleEntity.getRole());
        findRoleJpaEntity.setDescription(roleEntity.getDescription());

        if (Objects.isNull(roleEntity.getParent())) {
            findRoleJpaEntity.setParent(null);
        } else {
            RoleJpaEntity parent = roleJpaRepository.findByRole(roleEntity.getParent())
                    .orElseThrow(() -> new NotFoundDataException(roleEntity.getParent().name()));
            findRoleJpaEntity.setParent(parent);
        }

        if (roleEntity.getChildren().size() == 0) {
            findRoleJpaEntity.setChildren(List.of());
        } else {
            List<RoleJpaEntity> children = roleJpaRepository.findInRoles(roleEntity.getChildren());
            if (children.size() == 0) throw new NotFoundDataException(roleEntity.getChildren().toString());
            findRoleJpaEntity.setChildren(children);
        }

    }
}
