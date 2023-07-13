package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
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

    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private Role role;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private RoleJpaEntity parent;

    @OneToMany(mappedBy = "parent")
    private final List<RoleJpaEntity> children = new ArrayList<>();

    public RoleJpaEntity(Role role, String description, RoleJpaEntity parent, List<RoleJpaEntity> children) {
        this.role = role;
        this.description = description;
        setParent(parent);
        children.forEach(c -> c.setParent(this));
    }

    public static RoleJpaEntity toJpaEntity(RoleEntity roleEntity, RoleJpaEntity parent) {
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

    private void setParent(RoleJpaEntity parent) {
        if (Objects.isNull(parent)) return;

        this.parent = parent;
        parent.children.add(this);
    }
}
