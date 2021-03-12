package com.tascigorkem.restaurantservice.domain.restaurant;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RestaurantService {

    Flux<RestaurantDto> getAllRestaurants();

    Mono<RestaurantDto> getRestaurantById(UUID id);

    Mono<RestaurantDto> addRestaurant(RestaurantDto restaurantDto);

    Mono<RestaurantDto> updateRestaurant(RestaurantDto fakeRestaurantDto);

    Mono<RestaurantDto> removeRestaurant(UUID id);

}
