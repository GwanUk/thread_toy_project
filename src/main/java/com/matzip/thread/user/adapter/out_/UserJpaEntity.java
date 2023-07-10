package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.model.JpaBaseTimeEntity;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.user.domain.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USER_")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserJpaEntity extends JpaBaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "USER_ID")
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

    UserEntity toDomainEntity() {
        return new UserEntity(
                username,
                nickname,
                password,
                role
        );
    }

    static UserJpaEntity fromDomainEntity(UserEntity userEntity) {
        return new UserJpaEntity(
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getPassword(),
                userEntity.getRole()
        );
    }
}
