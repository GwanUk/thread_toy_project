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
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RolePersistencePort rolePersistencePort;

    @Test
    @DisplayName("롤 권한 계층 조회")
    void getHierarchy() {
        // given
        given(rolePersistencePort.findAll()).willReturn(List.of(
                new RoleEntity(ROLE_USER, "유저 권한", List.of()),
                new RoleEntity(ROLE_VIP, "특별 권한", List.of()),
                new RoleEntity(ROLE_MANAGER, "매니저 권한", List.of()),
                new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of())
        ));

        // when
        String concatenateRoles = roleService.getHierarchy();

        // then
        assertThat(concatenateRoles).isEqualTo(
                """
                        ROLE_VIP > ROLE_USER
                        ROLE_ADMIN > ROLE_VIP
                        ROLE_ADMIN > ROLE_MANAGER
                        """
        );
    }

    @Test
    @DisplayName("롤 권한 데이터 없는 상태일때 계층 조회 시도")
    void getHierarchy_non_data() {
        // given
        given(rolePersistencePort.findAll()).willReturn(List.of());

        // when
        String concatenateRoles = roleService.getHierarchy();

        // then
        assertThat(concatenateRoles).isEmpty();
    }

    @Test
    @DisplayName("롤 권한 데이터 무 계층 상태일때 계층 조회 시도")
    void getHierarchy_non_hierarchy() {
        // given
        given(rolePersistencePort.findAll()).willReturn(List.of(
                new RoleEntity(ROLE_USER, "유저 권한", List.of()),
                new RoleEntity(ROLE_VIP, "특별 권한", List.of()),
                new RoleEntity(ROLE_MANAGER, "매니저 권한", List.of()),
                new RoleEntity(ROLE_ADMIN, "관리자 권한", List.of())
        ));

        // when
        String concatenateRoles = roleService.getHierarchy();

        // then
        assertThat(concatenateRoles).isEmpty();
    }
}