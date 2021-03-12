package com.tascigorkem.restaurantservice.api.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodControllerRequestDto {

    private String name;
    private boolean vegetable;
    private BigDecimal price;
    private String imageUrl;
}
