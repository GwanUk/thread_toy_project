package com.matzip.thread.security.model;


import com.matzip.thread.user.domain.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * UserDetails 구현한 User Wrapper class
 */
@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {

    private final UserEntity userEntity;

    public UserContext(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userEntity.getUsername(), userEntity.getPassword(), authorities);
        this.userEntity = userEntity;
    }
}
