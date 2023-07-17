package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.NullCheck;
import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.annotation.Validation;
import com.matzip.thread.common.exception.DuplicationApplicationConvention;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static java.util.Objects.isNull;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RolePersistencePort {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleJdbcTemplateRepository roleJdbcTemplateRepository;

    @Override
    public Optional<RoleEntity> findByRole(Role role) {
        Map<Long, RoleEntity> map = new HashMap<>();
        RoleEntity result = null;

        for (RoleJdbcDto dto : roleJdbcTemplateRepository.findByRoleWithChildren(role)) {
            Long id = dto.getRoleId();
            Long parentId = dto.getParentId();
            RoleEntity jpaEntity = dto.toJpaEntity();

            map.put(id, jpaEntity);

            String roleName = role.name();
            String findRoleName = jpaEntity.getRole().name();

            if (roleName.equals(findRoleName)) {
                result = jpaEntity;
            } else {
                map.get(parentId).addChild(jpaEntity);
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<RoleEntity> findAll() {
        List<RoleEntity> list = new ArrayList<>();
        Map<Long, RoleEntity> map = new HashMap<>();

        for (RoleJdbcDto dto : roleJdbcTemplateRepository.findAll()) {
            Long id = dto.getRoleId();
            Long parentId = dto.getParentId();
            RoleEntity jpaEntity = dto.toJpaEntity();

            map.put(id, jpaEntity);

            if (isNull(parentId)) {
                list.add(jpaEntity);
            } else {
                map.get(parentId).addChild(jpaEntity);
            }
        }
        return list;
    }

    @Override
    @Validation
    public void save(@NullCheck RoleEntity roleEntity) {
//        RoleJpaEntity parentRoleJpaEntity = null;
//        Role parent = roleEntity.getParent();
//        List<Role> children = roleEntity.getChildren();
//
//        if (nonNull(parent)) {
//            parentRoleJpaEntity = roleJpaRepository.findByRole(parent)
//                    .orElseThrow(() -> new NotFoundDataException(parent.name()));
//        }
//
//        RoleJpaEntity roleJpaEntity = RoleJpaEntity.from(roleEntity, parentRoleJpaEntity);
//        RoleJpaEntity savedRoleJpaEntity = roleJpaRepository.save(roleJpaEntity);
//
//        List<RoleJpaEntity> findChildren = roleJpaRepository.findInRoles(children);
//
//        if (children.size() != 0 && findChildren.size() == 0) {
//            throw new NotFoundDataException(children.toString());
//        }
//
//        savedRoleJpaEntity.setChildren(findChildren);
    }

    @Override
    @Validation
    public void update(Role role, @NullCheck RoleEntity roleEntity) {
//        Optional<RoleJpaEntity> optionalRoleJpaEntity = roleJpaRepository.findByRole(role);
//        if (optionalRoleJpaEntity.isEmpty()) {
//            save(roleEntity);
//            return;
//        }
//
//        RoleJpaEntity findRoleJpaEntity = optionalRoleJpaEntity.get();
//
//        roleEntity.validate();
//        Role updatingRole = roleEntity.getRole();
//        String updatingDescription = roleEntity.getDescription();
//        Role updatingParent = roleEntity.getParent();
//        List<Role> updatingChildren = roleEntity.getChildren();
//
//        checkDuplication(role, updatingRole);
//
//        findRoleJpaEntity.setRole(updatingRole);
//        findRoleJpaEntity.setDescription(updatingDescription);
//
//        if (isNull(updatingParent)) {
//            findRoleJpaEntity.setParent(null);
//        } else {
//            RoleJpaEntity parent = roleJpaRepository.findByRole(updatingParent)
//                    .orElseThrow(() -> new NotFoundDataException(updatingParent.name()));
//            findRoleJpaEntity.setParent(parent);
//        }
//
//        if (updatingChildren.size() == 0) {
//            findRoleJpaEntity.setChildren(List.of());
//        } else {
//            List<RoleJpaEntity> children = roleJpaRepository.findInRoles(updatingChildren);
//            if (children.size() == 0) throw new NotFoundDataException(updatingChildren.toString());
//            findRoleJpaEntity.setChildren(children);
//        }
    }

    private void checkDuplication(Role role, Role updatingRole) {
        if (!role.equals(updatingRole)
                && roleJpaRepository.findByRole(updatingRole).isPresent()) {
            throw new DuplicationApplicationConvention(updatingRole.name());
        }
    }

    @Override
    public void delete(Role role) {

    }
}
