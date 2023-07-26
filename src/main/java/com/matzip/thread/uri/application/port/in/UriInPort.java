package com.matzip.thread.uri.application.port.in;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.Optional;

public interface UriInPort extends UriAllPort {
    void save(UriEntity uriEntity);

    Optional<UriEntity> findByuRi(String uri);
}
