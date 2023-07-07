package com.matzip.thread.users.application.port.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

    private String username;
    private String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validate() {
        return StringUtils.hasText(username) && StringUtils.hasText(password);
    }
}
