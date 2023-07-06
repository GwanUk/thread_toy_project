package com.matzip.thread.security.model;


import com.matzip.thread.users.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * UserDetails 구현한 User 객체를 상속받아서 유저 도메인 엔티티를 필드로 가짐
 */
@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {

    private final User user;

    public UserContext(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }
}
