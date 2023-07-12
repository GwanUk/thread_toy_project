package com.matzip.thread.uri.domain;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UriEntity {
    private final String uriName;
    private final int uriOrder;
    private final List<Role> roles = new ArrayList<>();

    public UriEntity(String uriName, int uriOrder, List<Role> roles) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
        this.roles.addAll(roles);
    }
}
