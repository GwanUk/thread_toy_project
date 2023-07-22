package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
class RoleSave {
    @NotNull
    private final Role role;
    private final String description;
    private final List<RoleSave> children = new ArrayList<>();

    RoleSave(Role role, String description, List<RoleSave> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
    }

    RoleEntity toEntity() {
        return new RoleEntity(role,
                description,
                children.stream()
                        .map(RoleSave::toEntity)
                        .toList());
    }
}
