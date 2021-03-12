package com.tascigorkem.restaurantservice.domain;

import com.github.javafaker.Faker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class DomainModelFaker {

    private static Faker faker = Faker.instance();

    public static CompanyDto getFakeCompanyDto(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return CompanyDto.builder()
                .id(id)
                .name(faker.company().name())
                .address(faker.address().fullAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .emailAddress(faker.internet().emailAddress())
                .websiteUrl(faker.internet().url())
                .build();
    }

    public static RestaurantDto getFakeRestaurantDto(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return RestaurantDto.builder()
                .id(id)
                .name(faker.lorem().word())
                .address(faker.address().fullAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .employeeCount(faker.number().numberBetween(1, 1000))
                .companyId(UUID.randomUUID())
                .build();
    }

    public static MenuDto getFakeMenuDto(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return MenuDto.builder()
                .id(id)
                .name(faker.lorem().word())
                .menuType(faker.lorem().word())
                .restaurantId(UUID.randomUUID())
                .build();
    }

    public static FoodDto getFakeFoodDto(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return FoodDto.builder()
                .id(id)
                .name(faker.food().dish())
                .vegetable(faker.bool().bool())
                .price(fakePrice())
                .imageUrl(faker.internet().url())
                .build();
    }

    public static BigDecimal fakePrice() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 1, 50));
    }

    public static MenuFoodDto getFakeMenuFoodDto(UUID id) {
        return MenuFoodDto.builder()
                .id(id)
                .menuId(UUID.randomUUID())
                .foodId(UUID.randomUUID())
                .foodName(faker.food().dish())
                .originalPrice(fakePrice())
                .extended(faker.bool().bool())
                .extendedPrice(fakePrice())
                .build();
    }

    public static UUID fakeId() {
        return UUID.randomUUID();
    }

}
