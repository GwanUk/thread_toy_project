package com.matzip.thread.security.service;

import com.matzip.thread.common.exception.securityexception.SignInFailedException;
import com.matzip.thread.security.token.ApiAuthenticationToken;
import com.matzip.thread.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증후 인증 객체와 권한이 담긴 인증 토큰 반환
     * @param authentication ApiAuthenticationToken principal = username, credentials = password
     * @return ApiAuthenticationToken principal = User, password = null, authorities
     * @throws AuthenticationException BadCredentialsException or UsernameNotFoundException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserEntity userEntity = ((UserContext) userDetails).getUserEntity();
        String encodedPassword = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (!isPasswordMatches(password, encodedPassword)) {
            throw new SignInFailedException();
        }

        return new ApiAuthenticationToken(userEntity, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ApiAuthenticationToken.class);
    }

    private boolean isPasswordMatches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
