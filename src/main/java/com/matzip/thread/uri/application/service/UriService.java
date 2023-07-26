package com.matzip.thread.uri.application.service;

import com.matzip.thread.common.annotation.PublishEvent;
import com.matzip.thread.uri.application.event.UriChangedEvent;
import com.matzip.thread.uri.application.port.in.UriInPort;
import com.matzip.thread.uri.application.port.out_.UriOutPort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UriService implements UriInPort {

    private final UriOutPort uriOutPort;

    @Override
    public List<UriEntity> findAll() {
        return uriOutPort.findAllWithRoles();
    }

    @Override
    public Optional<UriEntity> findByuRi(String uri) {
        return uriOutPort.findByUriWithRoles(uri);
    }

    @Override
    @PublishEvent(UriChangedEvent.class)
    @Transactional
    public void save(UriEntity uriEntity) {
        uriOutPort.save(uriEntity);
    }
}
