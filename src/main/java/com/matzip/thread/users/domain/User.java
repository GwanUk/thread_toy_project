package com.matzip.thread.users.domain;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String username;
    private final String password;
    private final Membership membership;

    public User(Long id, String username, String password, Membership membership) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.membership = membership;
    }

    public User(String username, String password, Membership membership) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.membership = membership;
    }
}
