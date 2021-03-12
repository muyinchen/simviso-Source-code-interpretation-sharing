package com.tascigorkem.restaurantservice.api.restaurant;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface RestaurantController {

    /**
     * Handles the incoming GET request "/restaurants"
     *
     * @return retrieve all non-deleted restaurants
     *
     * @see com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerResponseDto
     */
    @GetMapping("/restaurants")
    Mono<Response> getRestaurants();

    /**
     * Handles the incoming GET request "/restaurants/{id}"
     *
     * @param id of the restaurant to be retrieved
     * @return restaurant
     *
     * @see com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerResponseDto
     */
    @GetMapping("/restaurants/{id}")
    Mono<Response> getRestaurantById(@PathVariable("id") UUID id);

    /**
     * Handles the incoming POST request "/restaurants"
     *
     * @param restaurantControllerRequestDto fields of restaurant to be added
     * @return added restaurant
     *
     * @see com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerResponseDto
     */
    @PostMapping("/restaurants")
    Mono<Response> addRestaurant(@RequestBody RestaurantControllerRequestDto restaurantControllerRequestDto);

    /**
     * Handles the incoming PUT request "/restaurants/{id}"
     *
     * @param id of the restaurant to be updated
     * @param restaurantControllerRequestDto fields of restaurant to be updated
     * @return updated restaurant
     *
     * @see com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerResponseDto
     */
    @PutMapping("/restaurants/{id}")
    Mono<Response> updateRestaurant(@PathVariable("id") UUID id, @RequestBody RestaurantControllerRequestDto restaurantControllerRequestDto);

    /**
     * Handles the incoming DELETE request "/restaurants/{id}"
     *
     * @param id of the restaurant to be deleted
     * @return removed restaurant
     *
     * @see com.tascigorkem.restaurantservice.api.restaurant.RestaurantControllerResponseDto
     */
    @DeleteMapping("/restaurants/{id}")
    Mono<Response> removeRestaurant(@PathVariable("id") UUID id);

    Function<RestaurantDto, RestaurantControllerResponseDto> mapToRestaurantControllerResponseDto();

    Function<RestaurantControllerRequestDto, RestaurantDto> mapToRestaurantDto();
}
