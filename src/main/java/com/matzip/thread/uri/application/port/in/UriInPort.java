package com.matzip.thread.uri.application.port.in;

import com.matzip.thread.uri.domain.UriEntity;

import java.util.List;

public interface UriInPort {
    List<UriEntity> findAll();
}
