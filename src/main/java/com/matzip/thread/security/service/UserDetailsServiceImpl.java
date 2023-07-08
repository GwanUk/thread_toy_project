package com.matzip.thread.security.service;

import com.matzip.thread.common.exception.ApiAuthenticationException;
import com.matzip.thread.security.model.UserContext;
import com.matzip.thread.users.application.port.in.UserUseCase;
import com.matzip.thread.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserUseCase userUseCase;

    /**
     * 유저가 존재하면 UserContext 객체에 User 객체와 GrantedAuthority 리스트를 담아서 리턴
     * @param username the username identifying the user whose data is required.
     * @return UserContext implement UserDetails
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userUseCase.findByUsername(username)
                .orElseThrow(() -> new ApiAuthenticationException("The user does not exist"));

        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserContext(user, roles);
    }
}
