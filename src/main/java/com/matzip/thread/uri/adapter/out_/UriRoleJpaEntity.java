package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.role.adapter.out_.RoleJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "URI_ROLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UriRoleJpaEntity extends JpaBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "URI_ROLE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "URI_ID")
    private UriJpaEntity uriJpaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private RoleJpaEntity roleJpaEntity;

    UriRoleJpaEntity(UriJpaEntity uriJpaEntity, RoleJpaEntity roleJpaEntity) {
        setUri(uriJpaEntity);
        this.roleJpaEntity = roleJpaEntity;
    }

    void setUri(UriJpaEntity uriJpaEntity) {
        this.uriJpaEntity = uriJpaEntity;
        uriJpaEntity.addResourcesRolesJpaEntity(this);
    }
}
