package com.matzip.thread.users.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private Long id;
    private String username;
    private String password;
    private Role role;

    public User(String username, String password) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.role = null;
    }

    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, Role role) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
