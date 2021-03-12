package com.tascigorkem.restaurantservice.api.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuControllerRequestDto {

    private String name;
    private String menuType;
    private UUID restaurantId;
}
