package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class UriSaveRequest {
    private final String uriName;
    private final int uriOrder;
    private final List<Role> roles = new ArrayList<>();

    public UriSaveRequest(String uriName, int uriOrder, List<Role> roles) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
        this.roles.addAll(roles);
    }

    UriEntity toEntity() {
        return new UriEntity(uriName, uriOrder, roles);
    }
}
