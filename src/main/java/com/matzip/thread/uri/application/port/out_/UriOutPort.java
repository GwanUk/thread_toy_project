package com.matzip.thread.uri.application.port.out_;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.List;

public interface UriOutPort {
    List<UriEntity> findAllWithRoles();
}
