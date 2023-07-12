package com.matzip.thread.uri.application.service;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UriServiceTest {

    @InjectMocks
    private UriService uriService;
    @Mock
    private UriOutPort uriOutPort;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    @DisplayName("uri 자원 전체 조회 서비스 성공")
    void findAll() {
        // given
        BDDMockito.given(uriService.findAll()).willReturn(List.of(
                new UriEntity("/api/user/**", 1,  List.of(new RoleEntity(Role.ROLE_USER, "유저 권한"))),
                new UriEntity("/api/admin/**", 2, List.of(new RoleEntity(Role.ROLE_ADMIN, "특급 권한")))));

        // when
        List<UriEntity> findUriEntities = uriService.findAll();

        // then
        BDDAssertions.then(findUriEntities.get(0).getUriName()).isEqualTo("/api/user/**");
        BDDAssertions.then(findUriEntities.get(0).getUriOrder()).isEqualTo(1);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(findUriEntities.get(0).getRoles().get(0).getDescription()).isEqualTo("유저 권한");
        BDDAssertions.then(findUriEntities.get(1).getUriName()).isEqualTo("/api/admin/**");
        BDDAssertions.then(findUriEntities.get(1).getUriOrder()).isEqualTo(2);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getRole()).isEqualTo(Role.ROLE_ADMIN);
        BDDAssertions.then(findUriEntities.get(1).getRoles().get(0).getDescription()).isEqualTo("특급 권한");
    }

    @Test
    @DisplayName("uri 자원 저장 서비스 성공")
    void save_uri() {
        // given
        UriEntity uerEntity = new UriEntity("/api/user/**", 1, List.of());

        // when
        uriService.save(uerEntity);

        // then
        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
        BDDMockito.then(uriOutPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriName()).isEqualTo("/api/user/**");
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriOrder()).isEqualTo(1);
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles()).isEmpty();
    }

    @Test
    @DisplayName("uri with role 자원 저장 서비스 성공")
    void save_uri_role() {
        // given
        UriEntity uerEntity = new UriEntity("/api/user/**", 1, List.of(new RoleEntity(Role.ROLE_USER, "유저 자원")));

        // when
        uriService.save(uerEntity);

        // then
        ArgumentCaptor<UriEntity> uriEntityArgumentCaptor = ArgumentCaptor.forClass(UriEntity.class);
        BDDMockito.then(uriOutPort).should(Mockito.times(1)).save(uriEntityArgumentCaptor.capture());
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriName()).isEqualTo("/api/user/**");
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getUriOrder()).isEqualTo(1);
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles().get(0).getRole()).isEqualTo(Role.ROLE_USER);
        BDDAssertions.then(uriEntityArgumentCaptor.getValue().getRoles().get(0).getDescription()).isEqualTo("유저 자원");
    }
}