package com.matzip.thread.uri.application.port.in;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.Optional;

public interface UriInPort extends UriAllPort {
    Optional<UriEntity> findByUri(String uri);

    void save(UriEntity uriEntity);
}
