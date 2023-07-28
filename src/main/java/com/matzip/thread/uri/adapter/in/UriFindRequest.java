package com.matzip.thread.uri.adapter.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
class UriFindRequest {
    @NotBlank
    private final String uri;

    @JsonCreator
    UriFindRequest(String uri) {
        this.uri = uri;
    }
}
