package com.matzip.thread.uri.application.port.out_;

import com.matzip.thread.uri.domain.UriEntity;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UriOutPort {
    List<UriEntity> findAllWithRoles();

    Optional<UriEntity> findByUriWithRoles(String uri);

    void save(@NonNull UriEntity uriEntity);
}
