package com.matzip.thread.uri.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.uri.domain.UriEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @Column(name = "URI_NAME",
            nullable = false,
            unique = true)
    private String uri;

    @Column(name = "URI_ORDER",
            nullable = false)
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

    public static UriJpaEntity from(UriEntity uriEntity, List<UriRoleJpaEntity> uriRoleJpaEntities) {
        return new UriJpaEntity(uriEntity.getUri(),
                uriEntity.getOrder(),
                uriRoleJpaEntities);
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
        this.uriRolesJpaEntities.forEach(ur -> ur.setUri(null));
        this.uriRolesJpaEntities.clear();

        this.uriRolesJpaEntities.addAll(uriRolesJpaEntities);
        this.uriRolesJpaEntities.forEach(ur -> ur.setUri(this));
    }

    public void changeOrder(int order) {
        this.order = order;
    }

    public void changeUriRoles(@NonNull List<UriRoleJpaEntity> uriRolesJpaEntities) {
        addAll(uriRolesJpaEntities);
    }
}
