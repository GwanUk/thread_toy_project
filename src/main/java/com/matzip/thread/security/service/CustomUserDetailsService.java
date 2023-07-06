package com.matzip.thread.security.service;

import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.User;
import com.matzip.thread.security.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserUseCase userUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userUseCase.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("The user does not exist: " + username);
        }

        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserContext(user, roles);
    }
}
