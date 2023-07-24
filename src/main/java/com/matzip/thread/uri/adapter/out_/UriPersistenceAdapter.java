package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
class UriPersistenceAdapter implements UriOutPort {

    private final UriJpaRepository uriJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UriRoleJpaRepository uriRoleJpaRepository;

    @Override
    public List<UriEntity> findAllWithRoles() {
        return uriJpaRepository.findAllWithRoles().stream().map(UriJpaEntity::toEntity).toList();
    }

    @Override
    public void save(UriEntity uriEntity) {
        UriJpaEntity uriJpaEntity = UriJpaEntity.fromEntity(uriEntity);

        uriJpaRepository.save(uriJpaEntity);

        roleJpaRepository.findInRoles(uriEntity.getRoles()).stream()
                .map(roleJpaEntity -> new UriRoleJpaEntity(uriJpaEntity, roleJpaEntity))
                .forEach(uriRoleJpaRepository::save);
    }
}
