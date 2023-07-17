package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleJdbcDto {
    private Long roleId;
    private String description;
    private String roleName;
    private Long parentId;

    public RoleEntity toJpaEntity() {
        return new RoleEntity(Role.valueOf(roleName), description, List.of());
    }
}
