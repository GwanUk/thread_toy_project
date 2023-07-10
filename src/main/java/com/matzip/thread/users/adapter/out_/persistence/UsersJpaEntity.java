package com.matzip.thread.users.adapter.out_.persistence;

import com.matzip.thread.common.model.JpaBaseTimeEntity;
import com.matzip.thread.users.domain.Role;
import com.matzip.thread.users.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersJpaEntity extends JpaBaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "users_id")
    private Long id;
    private String username;
    private String nickname;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    UsersJpaEntity(String username, String nickname, String password, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    User toDomainEntity() {
        return new User(
                username,
                nickname,
                password,
                role
        );
    }

    static UsersJpaEntity fromDomainEntity(User user) {
        return new UsersJpaEntity(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }
}
