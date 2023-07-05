package com.matzip.thread.users.application.port.in;

public interface UserUseCase {
    void join(UserCreateRequest userCreateRequest);

    void edit(Long id, UserUpdateRequest userUpdateRequest);

    void delete(Long id);

    void login(Long id, String password);
}
