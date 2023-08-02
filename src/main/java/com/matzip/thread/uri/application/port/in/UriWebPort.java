package com.matzip.thread.uri.application.port.in;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.Optional;

public interface UriWebPort extends UriSecurityPort {
    Optional<UriEntity> findByUri(String uri);

    void save(UriEntity uriEntity);

    void update(UriEntity uriEntity);

    void delete(String uri);
}
