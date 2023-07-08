package com.matzip.thread.security.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
public class SignInRequest {

    private final String username;
    private final String password;

    public boolean validate() {
        return StringUtils.hasText(username) && StringUtils.hasText(password);
    }
}
