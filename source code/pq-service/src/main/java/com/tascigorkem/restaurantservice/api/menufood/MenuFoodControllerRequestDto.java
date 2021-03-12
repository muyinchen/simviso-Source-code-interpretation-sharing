package com.tascigorkem.restaurantservice.api.menufood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuFoodControllerRequestDto {

    private boolean extended;
    private BigDecimal extendedPrice;
}
