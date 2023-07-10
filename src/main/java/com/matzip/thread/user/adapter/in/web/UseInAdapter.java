package com.matzip.thread.user.adapter.in.web;

import com.matzip.thread.user.application.port.in.UserInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UseInAdapter {

    private final UserInPort userInPort;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign_up")
    void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userInPort.signUp(signUpRequest.toDomainEntity(passwordEncoder));
    }

}
