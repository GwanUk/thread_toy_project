package com.matzip.thread.security.service;

import com.matzip.thread.common.exception.securityexception.SignInFailedException;
import com.matzip.thread.user.application.port.in.UserQueryInPort;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserQueryInPort userQueryInPort;

    /**
     * 유저가 존재하면 UserContext 객체에 User 객체와 GrantedAuthority 리스트를 담아서 리턴
     * @param username the username identifying the user whose data is required.
     * @return UserContext implement UserDetails
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userQueryInPort.findByUsername(username)
                .orElseThrow(SignInFailedException::new);

        List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(userEntity.getRole().name()));

        return new UserContext(userEntity, roles);
    }
}
