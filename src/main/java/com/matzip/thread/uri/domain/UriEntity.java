package com.matzip.thread.uri.domain;

import com.matzip.thread.common.exception.InvalidEntityException;
import com.matzip.thread.common.validator.Validator;
import com.matzip.thread.role.domain.Role;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
public class UriEntity implements Validator {
    private final String uri;
    private final int order;
    private final List<Role> roles = new ArrayList<>();

    public UriEntity(String uri, int order, List<Role> roles) {
        this.uri = uri;
        this.order = order;
        this.roles.addAll(roles);
    }

    @Override
    public void validate() {
        if (isNull(uri) || uri.length() == 0 || uri.charAt(0) != '/') {
            throw new InvalidEntityException("uri value is wrong", "UriEntity", "uri", uri);
        }
    }
}
