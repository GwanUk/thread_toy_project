package com.matzip.thread.uri.domain;

import com.matzip.thread.role.domain.Role;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UriEntity {
    private final String uri;
    private final int order;
    private final List<Role> roles = new ArrayList<>();

    public UriEntity(@NonNull String uri, int order, List<Role> roles) {
        this.uri = uri;
        this.order = order;
        this.roles.addAll(roles);
    }
}
