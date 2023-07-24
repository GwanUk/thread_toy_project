package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
class RoleUpdate {
    @NotNull
    private final Role role;
    private final String description;
    private final List<RoleUpdate> children = new ArrayList<>();

    RoleUpdate(Role role, String description, List<RoleUpdate> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
    }

    RoleEntity toEntity() {
        return new RoleEntity(role,
                description,
                children.stream()
                        .map(RoleUpdate::toEntity)
                        .toList());
    }
}
