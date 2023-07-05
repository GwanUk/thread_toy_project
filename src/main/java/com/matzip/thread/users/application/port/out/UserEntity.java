package com.matzip.thread.users.application.port.out;

import com.matzip.thread.users.domain.Membership;
import com.matzip.thread.users.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id @GeneratedValue
    private  Long id;
    private  String username;
    private  String password;
    private Membership membership;

    public UserEntity(Long id, String username, String password, Membership membership) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.membership = membership;
    }

    public User toDomainEntity() {
        return new User(
                id,
                username,
                password,
                membership
        );
    }

    public static UserEntity formDomainEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getMembership()
        );
    }
}
