package com.matzip.thread.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String username;
    private String nickname;
    private String password;
    private Role role;

    public User(String username, String nickname, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }
}
