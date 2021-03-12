package com.tascigorkem.restaurantservice.api.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyControllerRequestDto {

    private String name;
    private String address;
    private String phone;
    private String emailAddress;
    private String websiteUrl;
}
