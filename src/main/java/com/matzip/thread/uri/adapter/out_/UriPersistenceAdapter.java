package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.application.port.out_.UriPersistencePort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


@PersistenceAdapter
@RequiredArgsConstructor
class UriPersistenceAdapter implements UriPersistencePort {

    private final UriJpaRepository uriJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<UriEntity> findAllWithRoles() {
        return uriJpaRepository.findAllWithRoles().stream().map(UriJpaEntity::toEntity).toList();
    }

    @Override
    public Optional<UriEntity> findByUriWithRoles(String uri) {
        return uriJpaRepository.findByUri(uri).map(UriJpaEntity::toEntity);
    }

    @Override
    public void save(@NonNull UriEntity uriEntity) {
        List<Role> roles = uriEntity.getRoles();
        List<UriRoleJpaEntity> uriRoleJpaEntities = null;
        if (!roles.isEmpty()) {
            List<RoleJpaEntity> findRoles = roleJpaRepository.findInRoles(roles);
            uriRoleJpaEntities = findRoles.stream()
                    .map(UriRoleJpaEntity::new)
                    .toList();
        }

        UriJpaEntity uriJpaEntity = UriJpaEntity.from(uriEntity, uriRoleJpaEntities);
        uriJpaRepository.save(uriJpaEntity);
    }

    @Override
    public void update(@NonNull String uri, @NonNull UriEntity uriEntity) {

        UriJpaEntity findUriEntity = uriJpaRepository.findByUri(uri)
                .orElseThrow(() -> new NotFoundDataException(uri));

        int order = uriEntity.getOrder();
        findUriEntity.changeOrder(order);

        List<Role> roles = uriEntity.getRoles();
        List<RoleJpaEntity> findRoles = roleJpaRepository.findInRoles(roles);
        List<UriRoleJpaEntity> uriRoleJpaEntities = findRoles.stream()
                .map(UriRoleJpaEntity::new)
                .toList();
        findUriEntity.changeUriRoles(uriRoleJpaEntities);
    }

    @Override
    public void delete(@NonNull String uri) {
        uriJpaRepository.deleteByUri(uri);
    }
}
