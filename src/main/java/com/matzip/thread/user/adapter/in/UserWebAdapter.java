package com.matzip.thread.user.adapter.in;

import com.matzip.thread.common.annotation.WebAdapter;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.application.port.in.UserInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@WebAdapter(path = "/api/users")
@RequiredArgsConstructor
class UserWebAdapter {

    private final UserInPort userInPort;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign_up")
    void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userInPort.signUp(signUpRequest.toEntity(passwordEncoder), Role.ROLE_USER);
    }
}
