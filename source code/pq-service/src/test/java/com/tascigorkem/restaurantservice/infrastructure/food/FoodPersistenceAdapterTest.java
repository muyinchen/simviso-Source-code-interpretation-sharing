package com.tascigorkem.restaurantservice.infrastructure.food;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FoodPersistenceAdapterTest {

    private final FoodRepository foodRepository = mock(FoodRepository.class);
    private final FoodPersistenceAdapter subject = new FoodPersistenceAdapter(foodRepository);

    /**
     * Unit test for FoodPersistenceAdapter:getAllFoods
     */
    @Test
    void testGetAllFoods() {
        // arrange
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(DomainModelFaker.fakeId());
        List<FoodDto> foodDtoList = Arrays.asList(fakeFoodDto, fakeFoodDto, fakeFoodDto);
        List<FoodEntity> foodEntityList = foodDtoList.stream().map(subject::mapToFoodEntity).collect(Collectors.toList());
        when(foodRepository.findAll()).thenReturn(Flux.fromIterable(foodEntityList));

        // act
        Flux<FoodDto> result = subject.getAllFoods();

        //assert
        StepVerifier.create(result)
                .expectNext(foodDtoList.get(0))
                .expectNext(foodDtoList.get(1))
                .expectNext(foodDtoList.get(2))
                .expectComplete()
                .verify();

        verify(foodRepository).findAll();
    }

    /**
     * Unit test for FoodPersistenceAdapter:getFoodById
     */
    @Test
    void getFoodById() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        FoodEntity fakeFoodEntity = subject.mapToFoodEntity(fakeFoodDto);
        when(foodRepository.findById(fakeFoodId)).thenReturn(Mono.just(fakeFoodEntity));

        // act
        Mono<FoodDto> result = subject.getFoodById(fakeFoodId);

        // assert
        StepVerifier.create(result)
                .assertNext(foodEntity ->
                        assertThat(foodEntity)
                                .usingRecursiveComparison()
                                .isEqualTo(fakeFoodDto))
                .verifyComplete();
    }

    /**
     * Unit test for FoodPersistenceAdapter:addFood
     */
    @Test
    void givenFoodControllerRequestDto_whenCreateFood_thenReturnSuccessful_andReturnFood() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        FoodEntity fakeFoodEntity = subject.mapToFoodEntity(fakeFoodDto);
        when(foodRepository.save(any(FoodEntity.class))).thenReturn(Mono.just(fakeFoodEntity));

        // act
        Mono<FoodDto> result = subject.addFood(fakeFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodRepository).save(any(FoodEntity.class));
    }

    /**
     * Unit test for FoodPersistenceAdapter:updateFood
     */
    @Test
    void givenFoodId_andFoodDto_whenUpdateFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        FoodEntity fakeFoodEntity = subject.mapToFoodEntity(fakeFoodDto);
        when(foodRepository.findById(fakeFoodId)).thenReturn(Mono.just(fakeFoodEntity));
        when(foodRepository.save(fakeFoodEntity)).thenReturn(Mono.just(fakeFoodEntity));

        // act

        Mono<FoodDto> result = subject.updateFood(fakeFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodRepository).findById(fakeFoodId);
        verify(foodRepository).save(any(FoodEntity.class));
    }

    /**
     * Unit test for FoodPersistenceAdapter:removeFood
     */
    @Test
    void givenFoodId_whenRemoveFood_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        FoodEntity fakeFoodEntity = subject.mapToFoodEntity(fakeFoodDto);
        when(foodRepository.findById(fakeFoodId)).thenReturn(Mono.just(fakeFoodEntity));
        when(foodRepository.save(fakeFoodEntity)).thenReturn(Mono.just(fakeFoodEntity));

        // act
        Mono<FoodDto> result = subject.removeFood(fakeFoodId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        verify(foodRepository).findById(fakeFoodId);
        verify(foodRepository).save(any(FoodEntity.class));
    }
}