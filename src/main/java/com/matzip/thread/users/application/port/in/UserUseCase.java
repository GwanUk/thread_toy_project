package com.matzip.thread.users.application.port.in;

public interface UserUseCase {
    void signUp(SignUpRequest signUpRequest);

    void edit(Long id, UserUpdateRequest userUpdateRequest);

    void delete(Long id);
}
