package com.matzip.thread.role.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleEntity {
    private final Long id;
    private final Role role;
    private final String description;
}
