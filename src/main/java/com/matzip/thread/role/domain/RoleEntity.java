package com.matzip.thread.role.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RoleEntity {
    private final Role role;
    private final String description;
    private final List<RoleEntity> children = new ArrayList<>();

    public RoleEntity(Role role, String description, List<RoleEntity> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
    }

    public String getHierarchyString() {
        StringBuilder stringBuilder = new StringBuilder();
        children.forEach(c -> stringBuilder.append(role.name())
                .append(" > ")
                .append(c.role.name()));
        return stringBuilder.toString();
    }

    public void addChild(RoleEntity roleEntity) {
        children.add(roleEntity);
    }

    public String getName() {
        return role.name();
    }
}
