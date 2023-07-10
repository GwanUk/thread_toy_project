package com.matzip.thread.role.adapter.out_;

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
    private String roleName;

    private String description;

    public RoleJpaEntity(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public RoleEntity toEntity() {
        return new RoleEntity(roleName, description);
    }
}
