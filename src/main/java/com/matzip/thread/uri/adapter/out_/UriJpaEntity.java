package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "URI")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UriJpaEntity extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "URI_ID")
    private Long id;

    @Column(name = "URI_NAME")
    private String uri;

    @Column(name = "URI_ORDER")
    private int order;

    @OneToMany(mappedBy = "uriJpaEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<UriRoleJpaEntity> uriRolesJpaEntities = new ArrayList<>();

    UriJpaEntity(String uri, int order, List<UriRoleJpaEntity> uriRolesJpaEntities) {
        this.uri = uri;
        this.order = order;
        addAll(uriRolesJpaEntities);
    }

    UriEntity toEntity() {
        return new UriEntity(
                uri,
                order,
                uriRolesJpaEntities.stream()
                        .map(UriRoleJpaEntity::getRole)
                        .toList());
    }

    private void addAll(List<UriRoleJpaEntity> uriRolesJpaEntities) {
        this.uriRolesJpaEntities.addAll(uriRolesJpaEntities);
        uriRolesJpaEntities.forEach(ur -> ur.setUri(this));
    }
}
