package com.matzip.thread.role.application.prot.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;

public interface RoleOutPort {
    RoleEntity findByRole(Role role);

    void save(RoleEntity roleEntity);
}
