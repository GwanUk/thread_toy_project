package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RoleUpdate {
    @NotNull
    private final Role role;
    private final String description;
    private final Role parent;
    private final List<Role> children = new ArrayList<>();

    RoleUpdate(Role role, String description, Role parent, List<Role> children) {
        this.role = role;
        this.description = description;
        this.parent = parent;
        this.children.addAll(children);
    }

    RoleEntity toEntity() {
        return new RoleEntity(role, description, parent, children);
    }
}
