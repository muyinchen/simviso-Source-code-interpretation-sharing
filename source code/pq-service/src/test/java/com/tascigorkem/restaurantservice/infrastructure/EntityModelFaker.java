package com.tascigorkem.restaurantservice.infrastructure;

import com.github.javafaker.Faker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodEntity;
import com.tascigorkem.restaurantservice.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class EntityModelFaker {
    private static Faker faker = Faker.instance();

    public static FoodEntity getFakeFoodEntity(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return FoodEntity.builder()
                .id(id)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(faker.food().dish())
                .vegetable(faker.bool().bool())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10)))
                .imageUrl(faker.internet().url())
                .build();
    }

    public static UUID fakeFoodId() {
        return UUID.randomUUID();
    }
}
