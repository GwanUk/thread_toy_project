package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class UriSaveRequest {
    private final String uri;
    private final int order;
    private final List<Role> roles = new ArrayList<>();

    public UriSaveRequest(String uri, int order, List<Role> roles) {
        this.uri = uri;
        this.order = order;
        this.roles.addAll(roles);
    }

    UriEntity toEntity() {
        return new UriEntity(uri, order, roles);
    }
}
