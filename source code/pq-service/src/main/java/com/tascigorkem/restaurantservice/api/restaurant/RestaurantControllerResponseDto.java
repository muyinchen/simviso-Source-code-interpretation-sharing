package com.tascigorkem.restaurantservice.api.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class RestaurantControllerResponseDto {

    private UUID id;
    private String name;
    private String address;
    private String phone;
    private int employeeCount;
    private UUID companyId;

}
