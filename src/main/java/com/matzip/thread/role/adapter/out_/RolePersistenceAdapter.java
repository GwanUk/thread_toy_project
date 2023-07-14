package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.NullCheck;
import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.annotation.Validation;
import com.matzip.thread.common.exception.security.ApplicationConventionViolationException;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RoleOutPort {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    @Validation
    public Optional<RoleEntity> findByRole(Role role) {
        return roleJpaRepository.findByRole(role)
                .map(RoleJpaEntity::toEntity);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(RoleJpaEntity::toEntity).toList();
    }

    @Override
    @Validation
    public void save(@NullCheck RoleEntity roleEntity) {
        RoleJpaEntity parentRoleJpaEntity = null;
        Role parent = roleEntity.getParent();
        List<Role> children = roleEntity.getChildren();

        if (nonNull(parent)) {
            parentRoleJpaEntity = roleJpaRepository.findByRole(parent)
                    .orElseThrow(() -> new NotFoundDataException(parent.name()));
        }

        RoleJpaEntity roleJpaEntity = RoleJpaEntity.from(roleEntity, parentRoleJpaEntity);
        RoleJpaEntity savedRoleJpaEntity = roleJpaRepository.save(roleJpaEntity);

        List<RoleJpaEntity> findChildren = roleJpaRepository.findInRoles(children);

        if (children.size() != 0 && findChildren.size() == 0) {
            throw new NotFoundDataException(children.toString());
        }

        savedRoleJpaEntity.setChildren(findChildren);
    }

    @Override
    @Validation
    public void update(Role role, @NullCheck RoleEntity roleEntity) {
        RoleJpaEntity findRoleJpaEntity = roleJpaRepository.findByRole(role)
                .orElseThrow(() -> new NotFoundDataException(role.name()));

        Role updatingRole = roleEntity.getRole();
        String updatingDescription = roleEntity.getDescription();
        Role updatingParent = roleEntity.getParent();
        List<Role> updatingChildren = roleEntity.getChildren();

        checkDuplication(role, updatingRole);

        findRoleJpaEntity.setRole(updatingRole);
        findRoleJpaEntity.setDescription(updatingDescription);

        if (isNull(updatingParent)) {
            findRoleJpaEntity.setParent(null);
        } else {
            RoleJpaEntity parent = roleJpaRepository.findByRole(updatingParent)
                    .orElseThrow(() -> new NotFoundDataException(updatingParent.name()));
            findRoleJpaEntity.setParent(parent);
        }

        if (updatingChildren.size() == 0) {
            findRoleJpaEntity.setChildren(List.of());
        } else {
            List<RoleJpaEntity> children = roleJpaRepository.findInRoles(updatingChildren);
            if (children.size() == 0) throw new NotFoundDataException(updatingChildren.toString());
            findRoleJpaEntity.setChildren(children);
        }
    }

    private void checkDuplication(Role role, Role updatingRole) {
        if (!role.equals(updatingRole)
                && roleJpaRepository.findByRole(updatingRole).isPresent()) {
            throw new ApplicationConventionViolationException(updatingRole.name() + " that already exists");
        }
    }

    @Override
    public void delete(Role role) {

    }
}
