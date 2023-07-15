package com.matzip.thread.role.application.service;

import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.role.application.prot.in.RoleHierarchyPort;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleService implements RoleWebPort, RoleHierarchyPort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public RoleEntity findByRole(Role role) {
        return rolePersistencePort.findByRole(role).orElseThrow(() -> new NotFoundDataException(role.name()));
    }

    @Override
    public List<RoleEntity> findAll() {
        return rolePersistencePort.findAll();
    }

    @Override
    public String getHierarchy() {
        return rolePersistencePort.findAll().stream()
                .filter(r -> nonNull(r.getParent()))
                .map(RoleEntity::getHierarchyString)
                .collect(joining());
    }

    @Override
    @Transactional
    public void save(RoleEntity roleEntity) {
        rolePersistencePort.save(roleEntity);
    }

    @Override
    public void update(Role role, RoleEntity roleEntity) {

    }

    @Override
    public void delete(Role role) {

    }
}
