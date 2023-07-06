package com.matzip.thread.users.application.port.out;

import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {

    @Id @GeneratedValue
    private  Long id;
    private String userId;
    private  String username;
    private  String password;
    private Role role;

    public UserEntity(String userId, String username, String password, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User toDomainEntity() {
        return new User(
                userId,
                username,
                password,
                role
        );
    }

    public static UserEntity formDomainEntity(User user) {
        return new UserEntity(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
