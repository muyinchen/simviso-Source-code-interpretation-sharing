package com.tascigorkem.restaurantservice.api.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.ApiModelFaker;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.food.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(FoodController.class)
class FoodControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private FoodService foodService;

    private FoodController subject = new FoodControllerImpl(foodService);

    /**
     * Unit test for FoodController:getAllFoods
     */
    @Test
    void testGetAllFoods() {
        // arrange
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(DomainModelFaker.fakeId());
        List<FoodDto> foodDtoList = Arrays.asList(fakeFoodDto, fakeFoodDto, fakeFoodDto);
        when(foodService.getAllFoods()).thenReturn(Flux.fromIterable(foodDtoList));

        // act
        client.get().uri("/foods")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    List<FoodControllerResponseDto> responseDtoList = Arrays.asList(
                            objectMapper.convertValue(response.getPayload(), FoodControllerResponseDto[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(responseDto ->
                            assertAll(
                                    () -> assertEquals(fakeFoodDto.getId(), responseDto.getId()),
                                    () -> assertEquals(fakeFoodDto.getName(), responseDto.getName()),
                                    () -> assertEquals(fakeFoodDto.isVegetable(), responseDto.isVegetable()),
                                    () -> assertEquals(fakeFoodDto.getPrice(), responseDto.getPrice()),
                                    () -> assertEquals(fakeFoodDto.getImageUrl(), responseDto.getImageUrl())
                            ));

                });
        verify(foodService).getAllFoods();
    }

    /**
     * Unit test for FoodController:getFoodById
     */
    @Test
    void givenFoodId_whenGetFood_andFoodExists_thenReturnFood() {
        // arrange
        UUID fakeFoodId = UUID.randomUUID();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodService.getFoodById(fakeFoodId)).thenReturn(Mono.just(fakeFoodDto));
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
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(fakeFoodId, foodResponseDto.getId()),
                            () -> assertEquals(fakeFoodDto.getName(), foodResponseDto.getName()),
                            () -> assertEquals(fakeFoodDto.isVegetable(), foodResponseDto.isVegetable()),
                            () -> assertEquals(fakeFoodDto.getPrice(), foodResponseDto.getPrice()),
                            () -> assertEquals(fakeFoodDto.getImageUrl(), foodResponseDto.getImageUrl())
                    );
                });
        verify(foodService).getFoodById(fakeFoodId);
    }

    /**
     * Unit test for FoodController:addFood
     */
    @Test
    void givenFoodControllerRequestDto_whenCreateFood_thenReturnSuccessful_andReturnFood() {
        // arrange
        FoodControllerRequestDto fakeFoodControllerRequestDto = ApiModelFaker.getFoodControllerRequestDto();
        FoodDto fakeFoodDto = subject.mapToFoodDto().apply(fakeFoodControllerRequestDto);
        when(foodService.addFood(fakeFoodDto)).thenReturn(Mono.just(fakeFoodDto));
        // act
        client.post().uri("/foods")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeFoodControllerRequestDto), FoodControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                            FoodControllerResponseDto foodResponseDto = objectMapper
                                    .convertValue(response.getPayload(), FoodControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeFoodDto.getName(), foodResponseDto.getName()),
                                    () -> assertEquals(fakeFoodDto.isVegetable(), foodResponseDto.isVegetable()),
                                    () -> assertEquals(fakeFoodDto.getPrice(), foodResponseDto.getPrice()),
                                    () -> assertEquals(fakeFoodDto.getImageUrl(), foodResponseDto.getImageUrl())
                            );
                        }

                );
        verify(foodService).addFood(fakeFoodDto);
    }

    /**
     * Unit test for FoodController:updateFood
     */
    @Test
    void givenFoodFoodControllerRequestDto_andFoodControllerRequestDto_whenUpdateFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = UUID.randomUUID();
        FoodControllerRequestDto fakeFoodControllerRequestDto = ApiModelFaker.getFoodControllerRequestDto();
        FoodDto fakeFoodDto = subject.mapToFoodDto().apply(fakeFoodControllerRequestDto);
        fakeFoodDto.setId(fakeFoodId);
        when(foodService.updateFood(fakeFoodDto)).thenReturn(Mono.just(fakeFoodDto));
        // act
        client.put().uri("/foods/" + fakeFoodId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeFoodControllerRequestDto), FoodControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    FoodControllerResponseDto foodResponseDto = objectMapper
                            .convertValue(response.getPayload(), FoodControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeFoodDto.getName(), foodResponseDto.getName()),
                                    () -> assertEquals(fakeFoodDto.isVegetable(), foodResponseDto.isVegetable()),
                                    () -> assertEquals(fakeFoodDto.getPrice(), foodResponseDto.getPrice()),
                                    () -> assertEquals(fakeFoodDto.getImageUrl(), foodResponseDto.getImageUrl())
                            );
                        }

                );
        verify(foodService).updateFood(fakeFoodDto);
    }

    /**
     * Unit test for FoodController:removeFood
     */
    @Test
    void givenFoodId_whenRemoveFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = UUID.randomUUID();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodService.removeFood(fakeFoodId)).thenReturn(Mono.just(fakeFoodDto));
        // act
        client.delete().uri("/foods/" + fakeFoodId)
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
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeFoodDto.getName(), foodResponseDto.getName()),
                                    () -> assertEquals(fakeFoodDto.isVegetable(), foodResponseDto.isVegetable()),
                                    () -> assertEquals(fakeFoodDto.getPrice(), foodResponseDto.getPrice()),
                                    () -> assertEquals(fakeFoodDto.getImageUrl(), foodResponseDto.getImageUrl())
                            );
                        }

                );
        verify(foodService).removeFood(fakeFoodId);
    }
}
