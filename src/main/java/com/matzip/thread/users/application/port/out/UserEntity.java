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
    private  String username;
    private  String password;
    private Role role;

    public UserEntity(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User toDomainEntity() {
        return new User(
                id,
                username,
                password,
                role
        );
    }

    public static UserEntity formDomainEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }
}
