package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.common.exception.NullArgumentException;
import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "ROLE_NAME",
            unique = true,
            nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private RoleJpaEntity parent;

    @OneToMany(mappedBy = "parent")
    private final List<RoleJpaEntity> children = new ArrayList<>();

    public RoleJpaEntity(Role role, String description, RoleJpaEntity parent, List<RoleJpaEntity> children) {
        setRole(role);
        setDescription(description);
        setParent(parent);
        setChildren(children);
    }

    public static RoleJpaEntity from(RoleEntity roleEntity, RoleJpaEntity parent) {
        return new RoleJpaEntity(
                roleEntity.getRole(),
                roleEntity.getDescription(),
                parent,
                List.of()
        );
    }

    public RoleEntity toEntity() {
        return new RoleEntity(
                role,
                description,
                Objects.nonNull(parent) ? parent.getRole() : null,
                children.stream()
                        .map(RoleJpaEntity::getRole)
                        .toList()
        );
    }

    public void setRole(Role role) {
        if (Objects.isNull(role)) throw new NullArgumentException(Role.class.toString());
        this.role = role;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(RoleJpaEntity parent) {
        if (Objects.nonNull(this.parent)) {
            this.parent.children.remove(this);
        }

        this.parent = parent;

        if (Objects.nonNull(parent)) {
            parent.children.add(this);
        }
    }

    public void setChildren(List<RoleJpaEntity> children) {
        if (Objects.isNull(children)) throw new NullArgumentException(Role.class.toString());

        children.forEach(c -> c.setParent(this));
    }
}
