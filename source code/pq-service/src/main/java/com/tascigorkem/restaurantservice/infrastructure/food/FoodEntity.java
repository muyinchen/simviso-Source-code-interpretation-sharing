package com.tascigorkem.restaurantservice.infrastructure.food;

import com.tascigorkem.restaurantservice.infrastructure.base.BaseEntity;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table("food")
public class FoodEntity extends BaseEntity {

    @Column("name")
    private String name;

    @Column("vegetable")
    private boolean vegetable;

    @Column("price")
    private BigDecimal price;

    @Column("image_url")
    private String imageUrl;

    @Builder

    public FoodEntity(LocalDateTime creationTime, LocalDateTime updateTime, Status status, LocalDateTime deletionTime, boolean deleted, UUID id, String name, boolean vegetable, BigDecimal price, String imageUrl) {
        super(creationTime, updateTime, status, deletionTime, deleted, id);
        this.name = name;
        this.vegetable = vegetable;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
