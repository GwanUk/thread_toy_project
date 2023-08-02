package com.matzip.thread.uri.application.service;

import com.matzip.thread.common.annotation.PublishEvent;
import com.matzip.thread.common.annotation.Retry;
import com.matzip.thread.common.exception.NullArgumentException;
import com.matzip.thread.uri.application.event.UriChangedEvent;
import com.matzip.thread.uri.application.port.in.UriWebPort;
import com.matzip.thread.uri.application.port.out_.UriPersistencePort;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UriService implements UriWebPort {

    private final UriPersistencePort uriPersistencePort;

    @Override
    public List<UriEntity> findAll() {
        return uriPersistencePort.findAllWithRoles();
    }

    @Override
    public Optional<UriEntity> findByUri(String uri) {
        return uriPersistencePort.findByUriWithRoles(uri);
    }

    @Override
    @PublishEvent(UriChangedEvent.class)
    @Transactional
    public void save(UriEntity uriEntity) {
        String uri = uriEntity.getUri();
        if (!StringUtils.hasText(uri)){
            throw new NullArgumentException("uri");
        }

        uriPersistencePort.save(uriEntity);
    }

    @Override
    @Retry
    @PublishEvent(UriChangedEvent.class)
    @Transactional
    public void update(UriEntity uriEntity) {
        String uri = uriEntity.getUri();
        if (!StringUtils.hasText(uri)){
            throw new NullArgumentException("uri");
        }

        uriPersistencePort.update(uri, uriEntity);
    }

    @Override
    public void delete(String uri) {
        uriPersistencePort.delete(uri);
    }
}
