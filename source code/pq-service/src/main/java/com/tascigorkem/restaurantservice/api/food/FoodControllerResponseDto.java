package com.tascigorkem.restaurantservice.api.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class FoodControllerResponseDto {

    private UUID id;
    private String name;
    private boolean vegetable;
    private BigDecimal price;
    private String imageUrl;

}
