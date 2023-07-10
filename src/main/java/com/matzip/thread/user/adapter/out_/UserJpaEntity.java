package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.model.JpaBaseTimeEntity;
import com.matzip.thread.user.domain.Role;
import com.matzip.thread.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserJpaEntity extends JpaBaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "users_id")
    private Long id;
    private String username;
    private String nickname;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    UserJpaEntity(String username, String nickname, String password, Role role) {
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

    static UserJpaEntity fromDomainEntity(User user) {
        return new UserJpaEntity(
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getRole()
        );
    }
}
