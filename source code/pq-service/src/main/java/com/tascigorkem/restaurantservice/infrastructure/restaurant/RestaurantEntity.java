package com.tascigorkem.restaurantservice.infrastructure.restaurant;

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
@Table("restaurant")
public class RestaurantEntity extends BaseEntity {

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("employee_count")
    private int employeeCount;

    @Column("company_id")
    private UUID companyId;

    @Builder
    public RestaurantEntity(LocalDateTime creationTime, LocalDateTime updateTime, Status status, LocalDateTime deletionTime, boolean deleted, UUID id, String name, String address, String phone, int employeeCount, UUID companyId) {
        super(creationTime, updateTime, status, deletionTime, deleted, id);
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.employeeCount = employeeCount;
        this.companyId = companyId;
    }
}
