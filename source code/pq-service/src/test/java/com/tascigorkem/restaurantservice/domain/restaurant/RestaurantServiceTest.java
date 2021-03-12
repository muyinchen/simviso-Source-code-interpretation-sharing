package com.tascigorkem.restaurantservice.domain.restaurant;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantPersistencePort;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantService;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    private final RestaurantPersistencePort restaurantPersistencePort = mock(RestaurantPersistencePort.class);
    private final RestaurantService subject = new RestaurantServiceImpl(restaurantPersistencePort);

    /**
     * Unit test for RestaurantService:getAllRestaurants
     */
    @Test
    void testGetAllRestaurants() {
        // arrange
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(DomainModelFaker.fakeId());
        List<RestaurantDto> restaurantDtoList = Arrays.asList(fakeRestaurantDto, fakeRestaurantDto, fakeRestaurantDto);
        when(restaurantPersistencePort.getAllRestaurants()).thenReturn(Flux.fromIterable(restaurantDtoList));

        // act
        Flux<RestaurantDto> result = subject.getAllRestaurants();

        //assert
        StepVerifier.create(result)
                .expectNext(restaurantDtoList.get(0))
                .expectNext(restaurantDtoList.get(1))
                .expectNext(restaurantDtoList.get(2))
                .expectComplete()
                .verify();

        verify(restaurantPersistencePort).getAllRestaurants();
    }

    /**
     * Unit test for RestaurantService:getRestaurantById
     */
    @Test
    void givenRestaurantId_whenGetRestaurant_andRestaurantExists_thenReturnRestaurant() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantPersistencePort.getRestaurantById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantDto));

        // act
        Mono<RestaurantDto> result = subject.getRestaurantById(fakeRestaurantId);

        // assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantPersistencePort).getRestaurantById(fakeRestaurantId);

    }

    /**
     * Unit test for RestaurantService:addRestaurant
     */
    @Test
    void givenRestaurantControllerRequestDto_whenCreateRestaurant_thenReturnSuccessful_andReturnRestaurant() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantPersistencePort.addRestaurant(fakeRestaurantDto)).thenReturn(Mono.just(fakeRestaurantDto));

        // act
        Mono<RestaurantDto> result = subject.addRestaurant(fakeRestaurantDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantPersistencePort).addRestaurant(fakeRestaurantDto);
    }


    /**
     * Unit test for RestaurantService:updateRestaurant
     */
    @Test
    void givenRestaurantId_andRestaurantDto_whenUpdateRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantPersistencePort.updateRestaurant(fakeRestaurantDto)).thenReturn(Mono.just(fakeRestaurantDto));

        // act
        Mono<RestaurantDto> result = subject.updateRestaurant(fakeRestaurantDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantPersistencePort).updateRestaurant(fakeRestaurantDto);
    }

    /**
     * Unit test for RestaurantService:removeRestaurant
     */
    @Test
    void givenRestaurantId_whenRemoveRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantPersistencePort.removeRestaurant(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantDto));

        // act
        Mono<RestaurantDto> result = subject.removeRestaurant(fakeRestaurantId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        verify(restaurantPersistencePort).removeRestaurant(fakeRestaurantId);
    }
}