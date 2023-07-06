package com.matzip.thread.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private String userId;
    private String username;
    private String password;
    private Role role;

    public User(String username, String password) {
        this.userId = null;
        this.username = username;
        this.password = password;
        this.role = null;
    }

    public User(String userId, String username, String password, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, Role role) {
        this.userId = null;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
