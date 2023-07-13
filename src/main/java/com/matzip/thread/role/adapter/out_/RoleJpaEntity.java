package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "ROLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleJpaEntity extends JpaBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private RoleJpaEntity parent;

    public RoleJpaEntity(Role role, String description, RoleJpaEntity parent) {
        this.role = role;
        this.description = description;
        this.parent = parent;
    }

    public static RoleJpaEntity fromEntity(RoleEntity roleEntity, RoleJpaEntity parentRoleJpaEntity) {
        return new RoleJpaEntity(roleEntity.getRole(), roleEntity.getDescription(), parentRoleJpaEntity);
    }

    public RoleEntity toEntity() {
        return new RoleEntity(role, description, Objects.nonNull(parent) ? parent.getRole() : null);
    }
}
