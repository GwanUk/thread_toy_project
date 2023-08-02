package com.matzip.thread.role.application.service;

import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.RoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.matzip.thread.role.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoleHierarchyServiceTest {

    @InjectMocks
    private RoleSecurityService roleHierarchyService;
    @Mock
    private RolePersistencePort rolePersistencePort;

    @Test
    @DisplayName("롤 권한 계층 조회")
    void getHierarchy() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of(user));
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of());
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of(manager));
        given(rolePersistencePort.findAll()).willReturn(List.of(admin, vip));

        // when
        String hierarchyString = roleHierarchyService.getHierarchy();

        // then
        assertThat(hierarchyString).isEqualTo("""
                  ROLE_ADMIN > ROLE_MANAGER
                  ROLE_VIP > ROLE_USER
                  """);
    }

    @Test
    @DisplayName("롤 권한 데이터 없는 상태일때 계층 조회 시도")
    void getHierarchy_non_data() {
        // given
        given(rolePersistencePort.findAll()).willReturn(List.of());

        // when
        String concatenateRoles = roleHierarchyService.getHierarchy();

        // then
        assertThat(concatenateRoles).isEmpty();
    }

    @Test
    @DisplayName("롤 권한 데이터 무 계층 상태일때 계층 조회 시도")
    void getHierarchy_non_hierarchy() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());
        RoleEntity vip = new RoleEntity(ROLE_VIP, "ROLE_VIP", List.of());
        RoleEntity manager = new RoleEntity(ROLE_MANAGER, "ROLE_MANAGER", List.of());
        RoleEntity admin = new RoleEntity(ROLE_ADMIN, "ROLE_ADMIN", List.of());
        given(rolePersistencePort.findAll()).willReturn(List.of(admin, manager, vip, user));

        // when
        String concatenateRoles = roleHierarchyService.getHierarchy();

        // then
        assertThat(concatenateRoles).isEmpty();
    }
}