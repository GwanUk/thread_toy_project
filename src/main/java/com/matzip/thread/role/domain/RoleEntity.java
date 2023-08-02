package com.matzip.thread.role.domain;

import com.matzip.thread.common.exception.InvalidEntityException;
import com.matzip.thread.common.validator.Validator;
import lombok.Getter;

import java.util.*;

import static java.util.Objects.*;

@Getter
public class RoleEntity implements Validator {
    private final Role role;
    private final String description;
    private final List<RoleEntity> children = new ArrayList<>();

    public RoleEntity(Role role, String description, List<RoleEntity> children) {
        this.role = role;
        this.description = description;
        if (nonNull(children)) {
            this.children.addAll(children);
        }
    }

    @Override
    public void validate() {
        if (isNull(role)) {
            throw new InvalidEntityException("role is null", "RoleEntity", "role", null);
        }
    }

    public String getHierarchyString() {
        StringBuilder sb = new StringBuilder();
        Queue<RoleEntity> queue = new LinkedList<>();
        queue.offer(this);

        while (!queue.isEmpty()) {
            RoleEntity parent = queue.poll();
            List<RoleEntity> children = parent.getChildren();
            children.forEach(c -> {
                sb.append(parent.getName())
                        .append(" > ")
                        .append(c.getName())
                        .append("\n");
                queue.offer(c);
            });
        }
        return sb.toString();
    }

    public void addChild(RoleEntity roleEntity) {
        children.add(roleEntity);
    }

    public String getName() {
        return role.name();
    }

    public boolean equalsRole(Role role) {
        return this.role.equals(role);
    }

}
