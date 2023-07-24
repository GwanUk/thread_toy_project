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

@Entity
@Getter
@Table(name = "ROLE_")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleJpaEntity extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private final List<RoleJpaEntity> children = new ArrayList<>();

//    public RoleJpaEntity(Role role, String description, RoleJpaEntity parent, List<RoleJpaEntity> children) {
//        setRole(role);
//        setDescription(description);
//        setParent(parent);
//        setChildren(children);
//    }
//
//    public static RoleJpaEntity from(RoleEntity roleEntity) {
//        return new RoleJpaEntity(
//                roleEntity.getRole(),
//                roleEntity.getDescription(),
//                null,
//                roleEntity.getChildren().stream()
//                        .map(RoleJpaEntity::from)
//                        .toList()
//        );
//    }
//
    public RoleEntity toEntity() {
        return new RoleEntity(
                role,
                description,
                children.stream()
                        .map(RoleJpaEntity::toEntity)
                        .toList()
        );
    }
//
//    public void setRole(Role role) {
//        if (isNull(role)) throw new NullArgumentException(Role.class.getSimpleName());
//        this.role = role;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    private void setParent(RoleJpaEntity parent) {
//        this.parent = parent;
//    }
//
//    public void setChildren(List<RoleJpaEntity> children)  {
//        if (isNull(children)) throw new NullArgumentException(Role.class.getSimpleName());
//
//        this.children.forEach(c -> c.setParent(null));
//
//        this.children.clear();
//
//        this.children.addAll(children);
//
//        children.forEach(c -> c.setParent(this));
//    }
}
