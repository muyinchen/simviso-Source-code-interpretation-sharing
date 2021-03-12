package com.tascigorkem.restaurantservice.infrastructure.menufood;

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
@Table("menu_food")
public class MenuFoodEntity extends BaseEntity {

    @Column("extended")
    private boolean extended;

    @Column("extended_price")
    private BigDecimal extendedPrice;

    @Column("menu_id")
    private UUID menuId;

    @Column("food_id")
    private UUID foodId;

    @Builder

    public MenuFoodEntity(LocalDateTime creationTime, LocalDateTime updateTime, Status status, LocalDateTime deletionTime, boolean deleted, UUID id, boolean extended, BigDecimal extendedPrice, UUID menuId, UUID foodId) {
        super(creationTime, updateTime, status, deletionTime, deleted, id);
        this.extended = extended;
        this.extendedPrice = extendedPrice;
        this.menuId = menuId;
        this.foodId = foodId;
    }
}
