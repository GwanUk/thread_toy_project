package com.matzip.thread.common.JpaEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "RESOURCES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourcesJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "resources_id")
    private Long id;

    private String uri;

    @OneToMany(mappedBy = "resourcesJpaEntity")
    private List<ResourcesRolesJpaEntity> resourcesRoles = new ArrayList<>();

    public ResourcesJpaEntity(Long id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public void addResourcesRolesJpaEntity(ResourcesRolesJpaEntity rolesJpaEntity) {
        resourcesRoles.add(rolesJpaEntity);
    }
}
