package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UriResponse {
    private final String uriName;
    private final int uriOrder;
    private final List<Role> roles;

    static UriResponse fromEntity(UriEntity uriEntity) {
        return new UriResponse(uriEntity.getUriName(), uriEntity.getUriOrder(), uriEntity.getRoles());
    }
}
