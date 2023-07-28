package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.exception.NullArgumentException;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@PersistenceAdapter
@RequiredArgsConstructor
class UriPersistenceAdapter implements UriOutPort {

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
    public void save(UriEntity uriEntity) {
        String uri = uriEntity.getUri();
        if (!StringUtils.hasText(uri)){
            throw new NullArgumentException("uri");
        }

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
}
