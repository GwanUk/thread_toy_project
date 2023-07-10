package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "ROLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String description;

    public RoleJpaEntity(Role role, String description) {
        this.role = role;
        this.description = description;
    }

    public static RoleJpaEntity fromEntity(RoleEntity roleEntity) {
        if (roleEntity == null)
            return null;
        return new RoleJpaEntity(roleEntity.getRole(), roleEntity.getDescription());
    }

    public RoleEntity toEntity() {
        return new RoleEntity(id, role, description);
    }
}
