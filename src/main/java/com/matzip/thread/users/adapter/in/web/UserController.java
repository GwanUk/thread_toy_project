package com.matzip.thread.users.adapter.in.web;

import com.matzip.thread.users.application.port.in.SignUpRequest;
import com.matzip.thread.users.application.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserUseCase userUseCase;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign_up")
    void signUp(@RequestBody SignUpRequest signUpRequest) {
        userUseCase.signUp(signUpRequest.passwordEncode(passwordEncoder));
    }

}
