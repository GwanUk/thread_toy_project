package com.matzip.thread.role.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class RoleResponse {
    private final Role role;
    private final String description;

    static RoleResponse fromEntity(RoleEntity roleEntity) {
        return new RoleResponse(roleEntity.getRole(), roleEntity.getDescription());
    }
}
