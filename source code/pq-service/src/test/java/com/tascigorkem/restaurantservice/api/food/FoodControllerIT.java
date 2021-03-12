package com.tascigorkem.restaurantservice.api.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.food.FoodPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FoodControllerIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @MockBean
    private FoodPersistencePort foodPersistencePort;

    @Test
    void getFood() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeFoodId = UUID.randomUUID();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodPersistencePort.getFoodById(fakeFoodId)).thenReturn(Mono.just(fakeFoodDto));

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
