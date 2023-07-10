package com.matzip.thread.users.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
    private final String username;
    private final String nickname;
    private final String password;
    private final Role role;
}
