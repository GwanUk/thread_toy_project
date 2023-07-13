package com.matzip.thread.role.application.service;

import com.matzip.thread.common.exception.NotfoundArgument;
import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleOutPort roleOutPort;

    @Test
    @DisplayName("role user 저장 서비스 성공")
    void save() {
        // given
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of());

        // when
        roleService.save(roleEntity);

        // then
        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);
        BDDMockito.then(roleOutPort).should(Mockito.times(1)).save(roleEntityArgumentCaptor.capture());
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getDescription()).isEqualTo("유저 권한");
    }

    @Test
    @DisplayName("role 조회 by user 권한 서비스 성공")
    void findByRole() {
        // given
        BDDMockito.given(roleOutPort.findByRole(Mockito.any())).willReturn(Optional.of(new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of())));

        // when
        RoleEntity findRoleEntity = roleService.findByRole(Role.ROLE_USER);

        // then
        BDDAssertions.then(findRoleEntity.getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findRoleEntity.getDescription()).isEqualTo("유저 권한");
    }

    @Test
    @DisplayName("role 조회 by user 권한 서비스 실패")
    void findByRole_failure() {
        // given
        BDDMockito.given(roleOutPort.findByRole(Mockito.any())).willReturn(Optional.empty());

        // expected
        BDDAssertions.thenThrownBy(() -> roleService.findByRole(Role.ROLE_USER)).isInstanceOf(NotfoundArgument.class);
    }

    @Test
    @DisplayName("role 권한 전체 조회 성공 서비스")
    void findAll() {
        // given
        BDDMockito.given(roleOutPort.findAll()).willReturn(List.of(
                new RoleEntity(Role.ROLE_USER, "유저 권한", null, List.of()),
                new RoleEntity(Role.ROLE_VIP, "특별 권한", null, List.of()),
                new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", null, List.of()),
                new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of())
        ));

        // when
        List<RoleEntity> roleEntities = roleService.findAll();

        // then
        BDDAssertions.then(roleEntities.get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntities.get(0).getDescription()).isEqualTo("유저 권한");

        BDDAssertions.then(roleEntities.get(1).getRole()).isEqualTo(Role.ROLE_VIP);
        BDDAssertions.then(roleEntities.get(1).getDescription()).isEqualTo("특별 권한");

        BDDAssertions.then(roleEntities.get(2).getRole()).isEqualTo(Role.ROLE_MANAGER);
        BDDAssertions.then(roleEntities.get(2).getDescription()).isEqualTo("매니저 권한");

        BDDAssertions.then(roleEntities.get(3).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(roleEntities.get(3).getDescription()).isEqualTo("관리자 권한");
    }

    @Test
    @DisplayName("롤 권한 계층화 성공")
    void getHierarchy() {
        // given
        BDDMockito.given(roleOutPort.findAll()).willReturn(List.of(
                new RoleEntity(Role.ROLE_USER, "유저 권한", Role.ROLE_VIP, List.of()),
                new RoleEntity(Role.ROLE_VIP, "특별 권한", Role.ROLE_ADMIN, List.of()),
                new RoleEntity(Role.ROLE_MANAGER, "매니저 권한", Role.ROLE_ADMIN, List.of()),
                new RoleEntity(Role.ROLE_ADMIN, "관리자 권한", null, List.of())
        ));


        // when
        String concatenateRoles = roleService.getHierarchy();

        // then
        BDDAssertions.then(concatenateRoles).isEqualTo(
                "ROLE_VIP > ROLE_USER\n" +
                        "ROLE_ADMIN > ROLE_VIP\n" +
                        "ROLE_ADMIN > ROLE_MANAGER\n"
        );


    }
}