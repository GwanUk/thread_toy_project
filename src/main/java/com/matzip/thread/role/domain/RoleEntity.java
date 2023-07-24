package com.matzip.thread.role.domain;

import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
public class RoleEntity{
    private final Role role;
    private final String description;
    private final List<RoleEntity> children = new ArrayList<>();

    public RoleEntity(@NonNull Role role, String description, List<RoleEntity> children) {
        this.role = role;
        this.description = description;
        this.children.addAll(children);
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
}
