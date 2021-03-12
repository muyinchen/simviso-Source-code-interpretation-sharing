package com.tascigorkem.restaurantservice.domain.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MenuDto {
    private UUID id;
    private String name;
    private String menuType;
    private UUID restaurantId;

}
