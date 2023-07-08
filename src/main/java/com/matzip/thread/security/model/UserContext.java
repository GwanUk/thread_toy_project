package com.matzip.thread.security.model;


import com.matzip.thread.users.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * UserDetails 구현한 User Wrapper class
 */
@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {

    private final User user;

    public UserContext(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }
}
