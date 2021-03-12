package com.tascigorkem.restaurantservice.api.menufood;

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
class MenuFoodControllerResponseDto {

    private UUID id;
    private UUID menuId;
    private UUID foodId;
    private String foodName;
    private BigDecimal originalPrice;
    private boolean extended;
    private BigDecimal extendedPrice;
}
