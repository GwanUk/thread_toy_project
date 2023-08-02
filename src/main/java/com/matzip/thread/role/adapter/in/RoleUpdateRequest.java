package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
class RoleUpdateRequest {
    @NotNull
    private final Role role;
    private final String description;
    private final List<RoleUpdateRequest> children = new ArrayList<>();

    RoleUpdateRequest(Role role, String description, List<RoleUpdateRequest> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
    }

    RoleEntity toEntity() {
        return new RoleEntity(role,
                description,
                children.stream()
                        .map(RoleUpdateRequest::toEntity)
                        .toList());
    }
}
