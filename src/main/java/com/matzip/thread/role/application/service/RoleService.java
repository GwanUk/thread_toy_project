package com.matzip.thread.role.application.service;

import com.matzip.thread.role.application.prot.in.RoleInPort;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RoleService implements RoleInPort {

    private final RoleOutPort roleOutPort;

    @Override
    public RoleEntity findByRole(Role role) {
        return roleOutPort.findByRole(role);
    }

    @Override
    public void save(RoleEntity roleEntity) {
        roleOutPort.save(roleEntity);
    }
}
