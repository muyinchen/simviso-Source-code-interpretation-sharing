package com.tascigorkem.restaurantservice.api;

import com.github.javafaker.Faker;
import com.tascigorkem.restaurantservice.api.company.CompanyControllerRequestDto;
import com.tascigorkem.restaurantservice.api.food.FoodControllerRequestDto;
import com.tascigorkem.restaurantservice.api.menu.MenuControllerRequestDto;
import com.tascigorkem.restaurantservice.api.menufood.MenuFoodControllerRequestDto;
import com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerRequestDto;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;

import java.math.BigDecimal;
import java.util.UUID;

public class ApiModelFaker {

    private static Faker faker = Faker.instance();

    public static CompanyControllerRequestDto getCompanyControllerRequestDto() {
        return CompanyControllerRequestDto.builder()
                .name(faker.company().name())
                .address(faker.address().fullAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .emailAddress(faker.internet().emailAddress())
                .websiteUrl(faker.internet().url())
                .build();
    }

    public static RestaurantControllerRequestDto getRestaurantControllerRequestDto() {
        return RestaurantControllerRequestDto.builder()
                .name(faker.lorem().word())
                .address(faker.address().fullAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .employeeCount(faker.number().numberBetween(1, 1000))
                .companyId(UUID.randomUUID())
                .build();
    }

    public static MenuControllerRequestDto getMenuControllerRequestDto() {
        return MenuControllerRequestDto.builder()
                .name(faker.lorem().word())
                .menuType(faker.lorem().word())
                .restaurantId(UUID.randomUUID())
                .build();
    }

    public static FoodControllerRequestDto getFoodControllerRequestDto() {
        return FoodControllerRequestDto.builder()
                .name(faker.food().dish())
                .vegetable(faker.bool().bool())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10)))
                .imageUrl(faker.internet().url())
                .build();
    }

    public static MenuFoodControllerRequestDto getMenuFoodControllerRequestDto() {
        return MenuFoodControllerRequestDto.builder()
                .extended(faker.bool().bool())
                .extendedPrice(DomainModelFaker.fakePrice())
                .build();
    }

    public static UUID fakeId() {
        return UUID.randomUUID();
    }

}
