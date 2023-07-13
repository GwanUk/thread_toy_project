package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class RoleSaveRequest {
    private final Role role;
    private final String description;
    private final Role parent;
    private final List<Role> roles = new ArrayList<>();

    public RoleSaveRequest(Role role, String description, Role parent, List<Role> roles) {
        this.role = role;
        this.description = description;
        this.parent = parent;
        this.roles.addAll(roles);
    }

    RoleEntity toEntity() {
        return new RoleEntity(role, description, parent, roles);
    }
}
