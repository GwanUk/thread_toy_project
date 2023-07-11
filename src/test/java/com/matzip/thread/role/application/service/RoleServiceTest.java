package com.matzip.thread.role.application.service;

import com.matzip.thread.role.application.prot.out_.RoleOutPort;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        RoleEntity roleEntity = new RoleEntity(Role.ROLE_USER, "유저 권한");

        // when
        roleService.save(roleEntity);
        
        // then
        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);
        BDDMockito.then(roleOutPort).should(Mockito.times(1)).save(roleEntityArgumentCaptor.capture());
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(roleEntityArgumentCaptor.getValue().getDescription()).isEqualTo("유저 권한");

    }
}