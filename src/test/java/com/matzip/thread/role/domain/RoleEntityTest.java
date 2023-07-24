package com.matzip.thread.role.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;

class RoleEntityTest {

    @Test
    @DisplayName("롤 계층 문자열")
    void getHierarchyString() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of(user));
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of(vip));
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(manager));

        // when
        String hierarchyString = admin.getHierarchyString();

        // then
        assertThat(hierarchyString).isEqualTo("""
                  ROLE_ADMIN > ROLE_MANAGER
                  ROLE_MANAGER > ROLE_VIP
                  ROLE_VIP > ROLE_USER
                  """);
    }

    @Test
    @DisplayName("롤 계층 문자열")
    void getHierarchyString_2() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of(user));
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of());
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(manager, vip));

        // when
        String hierarchyString = admin.getHierarchyString();

        // then
        assertThat(hierarchyString).isEqualTo("""
                  ROLE_ADMIN > ROLE_MANAGER
                  ROLE_ADMIN > ROLE_VIP
                  ROLE_VIP > ROLE_USER
                  """);
    }

    @Test
    @DisplayName("롤 계층 자식이 없으면 빈 문자열 반환")
    void getHierarchyString_empty() {
        // given
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of());

        // when
        String hierarchyString = admin.getHierarchyString();

        // then
        assertThat(hierarchyString).isEmpty();
    }
}