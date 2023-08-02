package com.matzip.thread.uri.application.port.out_;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.List;
import java.util.Optional;

public interface UriPersistencePort {
    List<UriEntity> findAllWithRoles();

    Optional<UriEntity> findByUriWithRoles(String uri);

    void save(UriEntity uriEntity);

    void update(String uri, UriEntity uriEntity);

    void delete(String uri);
}
