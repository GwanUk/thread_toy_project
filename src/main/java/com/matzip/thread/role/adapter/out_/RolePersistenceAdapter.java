package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.NullCheck;
import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.annotation.Validation;
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
        roleJpaRepository.save(RoleJpaEntity.from(roleEntity));
    }

    @Override
    @Validation
    public void update(Role role, @NullCheck RoleEntity roleEntity) {

    }

    @Override
    public void delete(Role role) {

    }
}
