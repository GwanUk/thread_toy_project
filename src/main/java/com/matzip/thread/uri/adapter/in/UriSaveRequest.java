package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UriSaveRequest {
    private final String uriName;
    private final int uriOrder;

    public UriEntity toEntity() {
        return new UriEntity(uriName, uriOrder);
    }
}
