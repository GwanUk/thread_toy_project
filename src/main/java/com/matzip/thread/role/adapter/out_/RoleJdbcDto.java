package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.hash;

@Getter
@Setter
class RoleJdbcDto{
    private Long roleId;
    private String roleName;
    private String description;
    private Long parentId;
    private String parentRoleName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    RoleJdbcDto() {
    }

    private RoleJdbcDto(String roleName, String description, String parentRoleName) {
        this.roleName = roleName;
        this.description = description;
        this.parentRoleName = parentRoleName;
    }

    static List<RoleJdbcDto> from(RoleEntity roleEntity) {
        ArrayList<RoleJdbcDto> dtoList = new ArrayList<>();
        RoleJdbcDto roleJdbcDto = new RoleJdbcDto(roleEntity.getName(), roleEntity.getDescription(), roleEntity.getName());
        dtoList.add(roleJdbcDto);

        Queue<RoleEntity> queue = new LinkedList<>();
        queue.offer(roleEntity);

        while (!queue.isEmpty()) {
            RoleEntity parent = queue.poll();
            List<RoleEntity> children = parent.getChildren();

            children.stream()
                    .map(c -> new RoleJdbcDto(
                            c.getName(),
                            c.getDescription(),
                            parent.getName()))
                    .forEach(dtoList::add);

            queue.addAll(children);
        }
        return dtoList;
    }

    RoleEntity toEntity() {
        return new RoleEntity(Role.valueOf(roleName), description, List.of());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleJdbcDto that = (RoleJdbcDto) o;
        return Objects.equals(roleName, that.roleName) && Objects.equals(description, that.description) && Objects.equals(parentRoleName, that.parentRoleName);
    }

    @Override
    public int hashCode() {
        return hash(roleName, description, parentRoleName);
    }
}
