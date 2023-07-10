package com.matzip.thread.uri.adapter.out_;

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
class UriJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "URI_ID")
    private Long id;

    @Column(name = "URI_NAME")
    private String uriName;

    @Column(name = "URI_ORDER")
    private int uriOrder;

    @OneToMany(mappedBy = "uriJpaEntity")
    private final List<UriRolesJpaEntity> uriRolesJpaEntities = new ArrayList<>();

    UriJpaEntity(String uriName, int uriOrder) {
        this.uriName = uriName;
        this.uriOrder = uriOrder;
    }

    void addResourcesRolesJpaEntity(UriRolesJpaEntity rolesJpaEntity) {
        uriRolesJpaEntities.add(rolesJpaEntity);
    }

    UriEntity toEntity() {
        return new UriEntity(
                uriName,
                uriOrder,
                uriRolesJpaEntities.stream()
                        .map(ur -> ur.getRoleJpaEntity().toEntity())
                        .toList());
    }

}
