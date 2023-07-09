package com.matzip.thread.common.JpaEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "RESOURCES_ROLES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcesRolesJpaEntity {
    @Id
    @GeneratedValue
    @Column(name = "resources_roles_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resources_id")
    private ResourcesJpaEntity resourcesJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id")
    private RolesJpaEntity rolesJpaEntity;

    @Column(name = "order_num")
    private int orderNum;
}
