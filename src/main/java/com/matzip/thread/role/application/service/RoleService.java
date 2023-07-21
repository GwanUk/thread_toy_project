package com.matzip.thread.role.application.service;

import com.matzip.thread.role.application.prot.in.RoleHierarchyPort;
import com.matzip.thread.role.application.prot.in.RoleWebPort;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleService implements RoleWebPort, RoleHierarchyPort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public Optional<RoleEntity> findByRole(Role role) {
        return rolePersistencePort.findByRole(role);
    }

    @Override
    public List<RoleEntity> findAll() {
        return rolePersistencePort.findAll();
    }

    @Override
    public String getHierarchy() {
        return rolePersistencePort.findAll().stream()
                .map(RoleEntity::getHierarchyString)
                .collect(Collectors.joining());
    }

    @Override
    @Transactional
    public void save(RoleEntity roleEntity) {
        rolePersistencePort.save(roleEntity);
    }

    @Override
    @Transactional
    public void update(Role role, RoleEntity roleEntity) {
        rolePersistencePort.update(role, roleEntity);
    }

    @Override
    @Transactional
    public void delete(Role role) {
        rolePersistencePort.delete(role);
    }
}
