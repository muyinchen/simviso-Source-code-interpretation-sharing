package com.tascigorkem.restaurantservice.api.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodEntity;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FoodControllerEnd2EndIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    void getFood() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeFoodId = UUID.randomUUID();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one entity
        foodRepository.deleteAll()
                .then(foodRepository.save(FoodEntity.builder()
                        .id(fakeFoodId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeFoodDto.getName())
                        .vegetable(fakeFoodDto.isVegetable())
                        .price(fakeFoodDto.getPrice())
                        .imageUrl(fakeFoodDto.getImageUrl())
                        .build()))
                .block();

        // act
        client.get().uri("/foods/" + fakeFoodId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    FoodControllerResponseDto foodResponseDto = objectMapper
                            .convertValue(response.getPayload(), FoodControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(fakeFoodId, foodResponseDto.getId()),
                            () -> assertEquals(fakeFoodDto.getName(), foodResponseDto.getName()),
                            () -> assertEquals(fakeFoodDto.isVegetable(), foodResponseDto.isVegetable()),
                            () -> assertEquals(fakeFoodDto.getPrice(), foodResponseDto.getPrice()),
                            () -> assertEquals(fakeFoodDto.getImageUrl(), foodResponseDto.getImageUrl())
                    );
                });
    }
}

