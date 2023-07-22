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
    private final List<RoleResponse> children = new ArrayList<>();

    public RoleResponse(Role role, String description, List<RoleResponse> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
    }

    static RoleResponse from(RoleEntity roleEntity) {
        return new RoleResponse(roleEntity.getRole(),
                roleEntity.getDescription(),
                roleEntity.getChildren().stream()
                        .map(RoleResponse::from)
                        .toList());
    }
}
