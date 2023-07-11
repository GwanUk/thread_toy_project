package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class UriSaveRequest {
    private final String uriName;
    private final int uriOrder;

    UriEntity toEntity() {
        return new UriEntity(uriName, uriOrder);
    }
}
