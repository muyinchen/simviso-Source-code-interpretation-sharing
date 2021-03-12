package com.tascigorkem.restaurantservice.infrastructure.company;

import com.tascigorkem.restaurantservice.infrastructure.base.BaseEntity;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table("company")
public class CompanyEntity extends BaseEntity {

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("email_address")
    private String emailAddress;

    @Column("website_url")
    private String websiteUrl;

    @Builder
    public CompanyEntity(LocalDateTime creationTime, LocalDateTime updateTime, Status status, LocalDateTime deletionTime, boolean deleted, UUID id, String name, String address, String phone, String emailAddress, String websiteUrl) {
        super(creationTime, updateTime, status, deletionTime, deleted, id);
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.websiteUrl = websiteUrl;
    }
}
