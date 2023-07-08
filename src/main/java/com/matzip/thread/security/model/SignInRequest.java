package com.matzip.thread.security.model;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class SignInRequest {

    private final String username;
    private final String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validate() {
        return StringUtils.hasText(username) && StringUtils.hasText(password);
    }
}
