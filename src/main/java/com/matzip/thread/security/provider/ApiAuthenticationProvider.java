package com.matzip.thread.security.provider;

import com.matzip.thread.security.model.UserContext;
import com.matzip.thread.security.token.ApiAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 인증토큰(생성자1)을 받아서 인증 시도, 성공시 인증토큰(생성자2) 리턴
     * @param authentication ApiAuthenticationToken 생성자1
     * @return ApiAuthenticationToken
     * @throws AuthenticationException BadCredentialsException or UsernameNotFoundException
     */
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserContext userContext = (UserContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userContext.getUser().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new ApiAuthenticationToken(userContext.getUser(), null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ApiAuthenticationToken.class);
    }
}
