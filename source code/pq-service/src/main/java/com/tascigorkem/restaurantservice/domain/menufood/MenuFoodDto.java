package com.tascigorkem.restaurantservice.domain.menufood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MenuFoodDto {

    private UUID id;
    private UUID menuId;
    private UUID foodId;
    private String foodName;
    private BigDecimal originalPrice;
    private boolean extended;
    private BigDecimal extendedPrice;

}