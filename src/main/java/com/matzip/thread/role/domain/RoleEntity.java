package com.matzip.thread.role.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RoleEntity {
    private final Role role;
    private final String description;
    private final Role parent;
    private final List<Role> children = new ArrayList<>();

    public RoleEntity(Role role, String description, Role parent, List<Role> children) {
        this.role = role;
        this.description = description;
        this.parent = parent;
        this.children.addAll(children);
    }
}
