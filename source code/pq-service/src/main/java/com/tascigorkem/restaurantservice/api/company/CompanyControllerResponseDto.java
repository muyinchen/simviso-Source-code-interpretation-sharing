package com.tascigorkem.restaurantservice.api.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyControllerResponseDto {

    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String emailAddress;
    private String websiteUrl;

}
