package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.role.domain.RoleEntity;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class UriSaveRequest {
    private final String uriName;
    private final int uriOrder;
    private final List<RoleEntity> roleEntities = new ArrayList<>();

    public UriSaveRequest(String uriName, int uriOrder, List<RoleEntity> roleEntities) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
        this.roleEntities.addAll(roleEntities);
    }

    UriEntity toEntity() {
        return new UriEntity(uriName, uriOrder, roleEntities);
    }
}
