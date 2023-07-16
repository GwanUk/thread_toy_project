package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class RoleResponse {
    private final Role role;
    private final String description;
    private final Role parent;
    private final List<Role> children = new ArrayList<>();

    public RoleResponse(Role role, String description, Role parent, List<Role> children) {
        this.role = role;
        this.description = description;
        this.parent = parent;
        this.children.addAll(children);
    }

    static RoleResponse toResponse(RoleEntity roleEntity) {
        return new RoleResponse(roleEntity.getRole(), roleEntity.getDescription(), null, List.of());
    }
}
