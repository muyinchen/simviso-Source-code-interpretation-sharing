package com.tascigorkem.restaurantservice.infrastructure.restaurant;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantEntity;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantPersistenceAdapter;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantRepository;
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

class RestaurantPersistenceAdapterTest {

    private final RestaurantRepository restaurantRepository = mock(RestaurantRepository.class);
    private final RestaurantPersistenceAdapter subject = new RestaurantPersistenceAdapter(restaurantRepository);

    /**
     * Unit test for RestaurantPersistenceAdapter:getAllRestaurants
     */
    @Test
    void testGetAllRestaurants() {
        // arrange
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(DomainModelFaker.fakeId());
        List<RestaurantDto> restaurantDtoList = Arrays.asList(fakeRestaurantDto, fakeRestaurantDto, fakeRestaurantDto);
        List<RestaurantEntity> restaurantEntityList = restaurantDtoList.stream().map(subject::mapToRestaurantEntity).collect(Collectors.toList());
        when(restaurantRepository.findAll()).thenReturn(Flux.fromIterable(restaurantEntityList));

        // act
        Flux<RestaurantDto> result = subject.getAllRestaurants();

        //assert
        StepVerifier.create(result)
                .expectNext(restaurantDtoList.get(0))
                .expectNext(restaurantDtoList.get(1))
                .expectNext(restaurantDtoList.get(2))
                .expectComplete()
                .verify();

        verify(restaurantRepository).findAll();
    }

    /**
     * Unit test for RestaurantPersistenceAdapter:getRestaurantById
     */
    @Test
    void getRestaurantById() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        RestaurantEntity fakeRestaurantEntity = subject.mapToRestaurantEntity(fakeRestaurantDto);
        when(restaurantRepository.findById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantEntity));

        // act
        Mono<RestaurantDto> result = subject.getRestaurantById(fakeRestaurantId);

        // assert
        StepVerifier.create(result)
                .assertNext(restaurantEntity ->
                        assertThat(restaurantEntity)
                                .usingRecursiveComparison()
                                .isEqualTo(fakeRestaurantDto))
                .verifyComplete();
    }

    /**
     * Unit test for RestaurantPersistenceAdapter:addRestaurant
     */
    @Test
    void givenRestaurantControllerRequestDto_whenCreateRestaurant_thenReturnSuccessful_andReturnRestaurant() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        RestaurantEntity fakeRestaurantEntity = subject.mapToRestaurantEntity(fakeRestaurantDto);
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(Mono.just(fakeRestaurantEntity));

        // act
        Mono<RestaurantDto> result = subject.addRestaurant(fakeRestaurantDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    /**
     * Unit test for RestaurantPersistenceAdapter:updateRestaurant
     */
    @Test
    void givenRestaurantId_andRestaurantDto_whenUpdateRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        RestaurantEntity fakeRestaurantEntity = subject.mapToRestaurantEntity(fakeRestaurantDto);
        when(restaurantRepository.findById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantEntity));
        when(restaurantRepository.save(fakeRestaurantEntity)).thenReturn(Mono.just(fakeRestaurantEntity));

        // act

        Mono<RestaurantDto> result = subject.updateRestaurant(fakeRestaurantDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantRepository).findById(fakeRestaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    /**
     * Unit test for RestaurantPersistenceAdapter:removeRestaurant
     */
    @Test
    void givenRestaurantId_whenRemoveRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        RestaurantEntity fakeRestaurantEntity = subject.mapToRestaurantEntity(fakeRestaurantDto);
        when(restaurantRepository.findById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantEntity));
        when(restaurantRepository.save(fakeRestaurantEntity)).thenReturn(Mono.just(fakeRestaurantEntity));

        // act
        Mono<RestaurantDto> result = subject.removeRestaurant(fakeRestaurantId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantRepository).findById(fakeRestaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }
}