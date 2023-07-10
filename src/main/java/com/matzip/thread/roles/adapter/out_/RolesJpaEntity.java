package com.matzip.thread.roles.adapter.out_;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "ROLES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RolesJpaEntity {

    @Id
    @GeneratedValue
    @Column(name = "roles_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    private String description;
}
