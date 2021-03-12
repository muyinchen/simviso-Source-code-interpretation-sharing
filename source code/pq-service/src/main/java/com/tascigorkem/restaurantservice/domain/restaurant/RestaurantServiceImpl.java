package com.tascigorkem.restaurantservice.domain.restaurant;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantPersistencePort restaurantPersistencePort;

    public RestaurantServiceImpl(RestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Flux<RestaurantDto> getAllRestaurants() {
        return restaurantPersistencePort.getAllRestaurants();
    }

    @Override
    public Mono<RestaurantDto> getRestaurantById(UUID id) {
        return restaurantPersistencePort.getRestaurantById(id);
    }

    @Override
    public Mono<RestaurantDto> addRestaurant(RestaurantDto restaurantDto) {
        return restaurantPersistencePort.addRestaurant(restaurantDto);
    }

    @Override
    public Mono<RestaurantDto> updateRestaurant(RestaurantDto restaurantDto) {
        return restaurantPersistencePort.updateRestaurant(restaurantDto);
    }

    @Override
    public Mono<RestaurantDto> removeRestaurant(UUID id) {
        return restaurantPersistencePort.removeRestaurant(id);
    }

}
