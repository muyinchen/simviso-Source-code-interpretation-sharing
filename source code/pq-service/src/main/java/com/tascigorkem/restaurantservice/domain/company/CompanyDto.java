package com.tascigorkem.restaurantservice.domain.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CompanyDto {

    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String emailAddress;
    private String websiteUrl;

}
