package com.tascigorkem.restaurantservice.infrastructure.menu;

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
@Table("menu")
public class MenuEntity extends BaseEntity {

    @Column("name")
    private String name;

    @Column("menu_type")
    private String menuType;

    @Column("restaurant_id")
    private UUID restaurantId;

    @Builder
    public MenuEntity(LocalDateTime creationTime, LocalDateTime updateTime, Status status, LocalDateTime deletionTime, boolean deleted, UUID id, String name, String menuType, UUID restaurantId) {
        super(creationTime, updateTime, status, deletionTime, deleted, id);
        this.name = name;
        this.menuType = menuType;
        this.restaurantId = restaurantId;
    }
}
