package com.tascigorkem.restaurantservice.domain.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class FoodDto {
    private UUID id;
    private String name;
    private boolean vegetable;
    private BigDecimal price;
    private String imageUrl;
}
