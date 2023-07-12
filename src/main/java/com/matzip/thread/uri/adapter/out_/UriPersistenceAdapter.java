package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.annotation.PersistenceAdapter;
import com.matzip.thread.common.exception.NotfoundArgument;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaRepository;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
class UriPersistenceAdapter implements UriOutPort {

    private final UriJapRepository uriJapRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<UriEntity> findAllWithRoles() {
        return uriJapRepository.findAllWithRoles().stream().map(UriJpaEntity::toEntity).toList();
    }

    @Override
    public void save(UriEntity uriEntity) {
        //TODO:role 도 함꼐 저장해야남
//        List<RoleJpaEntity> roleJpaEntities = uriEntity.getRoles().stream()
//                .map(re -> roleJpaRepository.findByRole(re.getRole()).orElseThrow(() -> new NotfoundArgument(re.getRole().name())))
//                .toList();


        uriJapRepository.save(UriJpaEntity.fromEntity(uriEntity));
    }
}
