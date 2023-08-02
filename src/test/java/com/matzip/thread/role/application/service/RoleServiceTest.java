package com.matzip.thread.role.application.service;

import com.matzip.thread.common.exception.UpdateTargetMismatchException;
import com.matzip.thread.role.application.prot.out_.RolePersistencePort;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.matzip.thread.role.domain.Role.ROLE_ADMIN;
import static com.matzip.thread.role.domain.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RolePersistencePort rolePersistencePort;

    @Test
    @DisplayName("권한 변경 시도")
    void update_roleName() {
        // given
        RoleEntity user = new RoleEntity(ROLE_USER, "ROLE_USER", List.of());

        // when
        Assertions.assertThatThrownBy(() -> roleService.update(ROLE_ADMIN, user))
                .isInstanceOf(UpdateTargetMismatchException.class)
                .hasMessage("Update target is different (ROLE_ADMIN <> ROLE_USER)");
    }

}