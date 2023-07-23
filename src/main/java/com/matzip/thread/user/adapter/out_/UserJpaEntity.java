package com.matzip.thread.user.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseTimeEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import com.matzip.thread.user.domain.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserJpaEntity extends JpaBaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    private String username;
    private String nickname;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private RoleJpaEntity roleJpaEntity;

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
                roleJpaEntity.toEntity().getRole()
        );
    }

    static UserJpaEntity fromEntity(UserEntity userEntity, RoleJpaEntity roleJpaEntity) {
        return new UserJpaEntity(
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getPassword(),
                roleJpaEntity
        );
    }
}
