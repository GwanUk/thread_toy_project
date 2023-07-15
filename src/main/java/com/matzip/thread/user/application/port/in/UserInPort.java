package com.matzip.thread.user.application.port.in;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.UserEntity;

public interface UserInPort extends UserQueryInPort{
    void signUp(UserEntity userEntity, Role role);
}
