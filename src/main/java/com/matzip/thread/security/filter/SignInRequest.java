package com.matzip.thread.security.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
class SignInRequest {

    private final String username;
    private final String password;

    boolean validate() {
        return StringUtils.hasText(username) && StringUtils.hasText(password);
    }
}
