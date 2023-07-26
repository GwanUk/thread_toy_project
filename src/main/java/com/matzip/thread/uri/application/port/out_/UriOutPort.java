package com.matzip.thread.uri.application.port.out_;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.List;
import java.util.Optional;

public interface UriOutPort {
    List<UriEntity> findAllWithRoles();

    void save(UriEntity uriEntity);

    Optional<UriEntity> findByUriWithRoles(String uri);
}
