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

    public RoleEntity toEntity() {
        return new RoleEntity(
                role,
                description,
                children.stream()
                        .map(RoleJpaEntity::toEntity)
                        .toList()
        );
    }
}
