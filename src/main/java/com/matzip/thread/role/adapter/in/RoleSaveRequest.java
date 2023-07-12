package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class RoleSaveRequest {
    private final Role role;
    private final String description;
    private final Role parent;

    RoleEntity toEntity() {
        return new RoleEntity(role, description, parent);
    }
}
