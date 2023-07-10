package com.matzip.thread.role.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleEntity {
    private final String roleName;
    private final String description;
}
