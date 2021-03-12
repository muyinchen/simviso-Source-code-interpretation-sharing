package com.tascigorkem.restaurantservice.domain.food;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FoodServiceTest {

    private final FoodPersistencePort foodPersistencePort = mock(FoodPersistencePort.class);
    private final FoodService subject = new FoodServiceImpl(foodPersistencePort);

    /**
     * Unit test for FoodService:getAllFoods
     */
    @Test
    void testGetAllFoods() {
        // arrange
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(DomainModelFaker.fakeId());
        List<FoodDto> foodDtoList = Arrays.asList(fakeFoodDto, fakeFoodDto, fakeFoodDto);
        when(foodPersistencePort.getAllFoods()).thenReturn(Flux.fromIterable(foodDtoList));

        // act
        Flux<FoodDto> result = subject.getAllFoods();

        //assert
        StepVerifier.create(result)
                .expectNext(foodDtoList.get(0))
                .expectNext(foodDtoList.get(1))
                .expectNext(foodDtoList.get(2))
                .expectComplete()
                .verify();

        verify(foodPersistencePort).getAllFoods();
    }

    /**
     * Unit test for FoodService:getFoodById
     */
    @Test
    void givenFoodId_whenGetFood_andFoodExists_thenReturnFood() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodPersistencePort.getFoodById(fakeFoodId)).thenReturn(Mono.just(fakeFoodDto));

        // act
        Mono<FoodDto> result = subject.getFoodById(fakeFoodId);

        // assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodPersistencePort).getFoodById(fakeFoodId);

    }

    /**
     * Unit test for FoodService:addFood
     */
    @Test
    void givenFoodControllerRequestDto_whenCreateFood_thenReturnSuccessful_andReturnFood() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodPersistencePort.addFood(fakeFoodDto)).thenReturn(Mono.just(fakeFoodDto));

        // act
        Mono<FoodDto> result = subject.addFood(fakeFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodPersistencePort).addFood(fakeFoodDto);
    }


    /**
     * Unit test for FoodService:updateFood
     */
    @Test
    void givenFoodId_andFoodDto_whenUpdateFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodPersistencePort.updateFood(fakeFoodDto)).thenReturn(Mono.just(fakeFoodDto));

        // act
        Mono<FoodDto> result = subject.updateFood(fakeFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodPersistencePort).updateFood(fakeFoodDto);
    }

    /**
     * Unit test for FoodService:removeFood
     */
    @Test
    void givenFoodId_whenRemoveFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        when(foodPersistencePort.removeFood(fakeFoodId)).thenReturn(Mono.just(fakeFoodDto));

        // act
        Mono<FoodDto> result = subject.removeFood(fakeFoodId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodPersistencePort).removeFood(fakeFoodId);
    }
}