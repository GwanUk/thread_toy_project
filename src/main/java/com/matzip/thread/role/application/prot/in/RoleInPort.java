package com.matzip.thread.role.application.prot.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;

public interface RoleInPort {
    RoleEntity findByRole(Role role);
}
