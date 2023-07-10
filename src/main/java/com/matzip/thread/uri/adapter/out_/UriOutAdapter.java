package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
class UriOutAdapter implements UriOutPort {

    private final UriJapRepository uriJapRepository;

    @Override
    public List<UriEntity> findAllWithRoles() {
        return uriJapRepository.findAllWithRoles().stream().map(UriJpaEntity::toEntity).toList();
    }
}
