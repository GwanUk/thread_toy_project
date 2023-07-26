package com.matzip.thread.uri.adapter.in;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
class UriFindRequest {
    @NotBlank
    private String uri;

    UriFindRequest() {
    }

    UriFindRequest(String uri) {
        this.uri = uri;
    }
}
