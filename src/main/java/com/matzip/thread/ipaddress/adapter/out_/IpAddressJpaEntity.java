package com.matzip.thread.ipaddress.adapter.out_;

import com.matzip.thread.common.JpaEntity.JpaBaseEntity;
import com.matzip.thread.ipaddress.domain.IpAddressEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "IP_ADDRESS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class IpAddressJpaEntity extends JpaBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "IP_ADDRESS_ID")
    private Long id;

    @Column(name = "IP_ADDRESS_NAME",
            nullable = false,
            unique = true)
    private String ipAddress;

    public IpAddressJpaEntity(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    IpAddressEntity toEntity() {
        return new IpAddressEntity(ipAddress);
    }
}
