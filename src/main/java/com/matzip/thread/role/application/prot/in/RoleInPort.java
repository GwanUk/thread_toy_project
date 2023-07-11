package com.matzip.thread.role.application.prot.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleInPort {
    RoleEntity findByRole(Role role);

    List<RoleEntity> findAll();

    void save(RoleEntity roleEntity);
}
