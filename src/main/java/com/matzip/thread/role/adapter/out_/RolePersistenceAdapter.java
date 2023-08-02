package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static java.util.Objects.isNull;

@PersistenceAdapter
@RequiredArgsConstructor
class RolePersistenceAdapter implements RolePersistencePort {

    private final RoleJdbcTemplateRepository roleJdbcTemplateRepository;

    @Override
    public Optional<RoleEntity> findByRole(Role role) {
        Map<Long, RoleEntity> parentMap = new HashMap<>();
        RoleEntity resultRoleEntity = null;

        for (RoleJdbcDto dto : roleJdbcTemplateRepository.findByRoleWithChildren(role)) {
            Long id = dto.getRoleId();
            Long parentId = dto.getParentId();
            RoleEntity roleEntity = dto.toEntity();

            parentMap.put(id, roleEntity);

            String roleName = role.name();
            String findRoleName = roleEntity.getName();

            if (roleName.equals(findRoleName)) {
                resultRoleEntity = roleEntity;
            } else {
                parentMap.get(parentId).addChild(roleEntity);
            }
        }
        return Optional.ofNullable(resultRoleEntity);
    }

    @Override
    public List<RoleEntity> findAll() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Map<Long, RoleEntity> parentMap = new HashMap<>();

        for (RoleJdbcDto dto : roleJdbcTemplateRepository.findAll()) {
            Long id = dto.getRoleId();
            Long parentId = dto.getParentId();
            RoleEntity jpaEntity = dto.toEntity();

            parentMap.put(id, jpaEntity);

            if (isNull(parentId)) {
                roleEntities.add(jpaEntity);
            } else {
                parentMap.get(parentId).addChild(jpaEntity);
            }
        }
        return roleEntities;
    }

    @Override
    public void save(RoleEntity roleEntity) {
        roleJdbcTemplateRepository.save(RoleJdbcDto.from(roleEntity));
    }

    /**
     * Role 권한은 변경 불가
     * @param role 변경 주체
     * @param roleEntity 변경 내용
     */
    @Override
    public void update(Role role, RoleEntity roleEntity) {
        roleJdbcTemplateRepository.update(role, RoleJdbcDto.from(roleEntity));
    }

    @Override
    public void delete(Role role) {
        roleJdbcTemplateRepository.delete(role);
    }
}
