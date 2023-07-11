package com.matzip.thread.uri.domain;

import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UriEntity {
    private final String uriName;
    private final int uriOrder;
    private final List<RoleEntity> roles = new ArrayList<>();

    public UriEntity(String uriName, int uriOrder) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
    }

    public UriEntity(String uriName, int uriOrder, List<RoleEntity> roles) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
        this.roles.addAll(roles);
    }
}
