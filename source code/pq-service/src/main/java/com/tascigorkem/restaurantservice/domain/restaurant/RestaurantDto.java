package com.tascigorkem.restaurantservice.domain.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private int employeeCount;
    private UUID companyId;
}
