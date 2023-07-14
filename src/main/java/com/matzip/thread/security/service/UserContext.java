package com.matzip.thread.security.service;


import com.matzip.thread.user.domain.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * UserDetails 구현한 User Wrapper class
 */
@Getter
class UserContext extends org.springframework.security.core.userdetails.User {

    private final UserEntity userEntity;

    UserContext(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userEntity.getUsername(), userEntity.getPassword(), authorities);
        this.userEntity = userEntity;
    }
}
