package com.matzip.thread.role.application.prot.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;

import java.util.List;

public interface RoleWebPort {
    RoleEntity findByRole(Role role);

    List<RoleEntity> findAll();

    void save(RoleEntity roleEntity);

    void update(Role role, RoleEntity roleEntity);

    void delete(Role role);
}
