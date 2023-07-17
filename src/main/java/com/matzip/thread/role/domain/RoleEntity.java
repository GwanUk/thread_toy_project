package com.matzip.thread.role.domain;

import com.matzip.thread.common.exception.NullArgumentException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
public class RoleEntity {
    private final Role role;
    private final String description;
    private final List<RoleEntity> children = new ArrayList<>();

    public RoleEntity(Role role, String description, List<RoleEntity> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
        validate();
    }

    public void validate() throws NullArgumentException {
        if (isNull(role)) throw new NullArgumentException("role");
    }

//    public String getHierarchyString() {
//        return parent.name() + " > " + role.name() + "\n";
//    }

    public void addChild(RoleEntity roleEntity) {
        children.add(roleEntity);
    }

    public String getName() {
        return role.name();
    }
}
