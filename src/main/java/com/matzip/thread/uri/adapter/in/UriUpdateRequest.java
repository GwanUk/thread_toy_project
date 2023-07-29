package com.matzip.thread.uri.adapter.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.Getter;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
class UriUpdateRequest {
    @NotBlank
    private final String uri;
    private final int order;
    private final List<Role> roles = new ArrayList<>();

    public UriUpdateRequest(@NonNull String uri, int order, List<Role> roles) {
        this.uri = uri;
        this.order = order;
        this.roles.addAll(roles);
    }

    UriEntity toEntity() {
        return new UriEntity(uri, order, roles);
    }
}
