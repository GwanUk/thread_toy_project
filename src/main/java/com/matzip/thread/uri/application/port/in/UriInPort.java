package com.matzip.thread.uri.application.port.in;

import com.matzip.thread.uri.domain.UriEntity;

public interface UriInPort extends UriQueryInPort{
    void save(UriEntity uriEntity);
}
