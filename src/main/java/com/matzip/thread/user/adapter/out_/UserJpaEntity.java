package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseTimeEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.user.domain.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Entity
@Getter
@Table(name = "USER_")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserJpaEntity extends JpaBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    private String username;
    private String nickname;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private RoleJpaEntity roleJpaEntity;
    @Version
    private Long version;

    UserJpaEntity(String username, String nickname, String password, RoleJpaEntity roleJpaEntity) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.roleJpaEntity = roleJpaEntity;
    }

    UserEntity toEntity() {
        return new UserEntity(
                username,
                nickname,
                password,
                isNull(roleJpaEntity) ? null : roleJpaEntity.getRole()
        );
    }

    static UserJpaEntity from(UserEntity userEntity, RoleJpaEntity roleJpaEntity) {
        return new UserJpaEntity(
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getPassword(),
                roleJpaEntity
        );
    }

    public void update(UserEntity userEntity, RoleJpaEntity roleJpaEntity) {
        this.nickname = userEntity.getNickname();
        this.password = userEntity.getPassword();
        this.roleJpaEntity = roleJpaEntity;
    }
}
