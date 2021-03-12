package com.tascigorkem.restaurantservice.api.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantPersistencePort;
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
class RestaurantControllerIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @MockBean
    private RestaurantPersistencePort restaurantPersistencePort;

    @Test
    void getRestaurant() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeRestaurantId = UUID.randomUUID();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantPersistencePort.getRestaurantById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantDto));

        // act
        client.get().uri("/restaurants/" + fakeRestaurantId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    RestaurantControllerResponseDto restaurantResponseDto = objectMapper
                            .convertValue(response.getPayload(), RestaurantControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(fakeRestaurantId, restaurantResponseDto.getId()),
                            () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                            () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                            () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                            () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                            () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                    );
                });

    }
}
