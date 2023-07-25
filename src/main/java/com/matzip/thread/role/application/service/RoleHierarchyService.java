package com.matzip.thread.role.application.service;

import com.matzip.thread.role.application.prot.in.RoleHierarchyPort;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleHierarchyService implements RoleHierarchyPort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public String getHierarchy() {
        return rolePersistencePort.findAll().stream()
                .map(RoleEntity::getHierarchyString)
                .collect(Collectors.joining());
    }
}
