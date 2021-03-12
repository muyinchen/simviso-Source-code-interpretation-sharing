package com.tascigorkem.restaurantservice.api.food;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface FoodController {
    /**
     * Handles the incoming GET request "/foods"
     *
     * @return retrieve all non-deleted foods
     *
     * @see com.tascigorkem.restaurantservice.api.food.FoodControllerResponseDto
     */
    @GetMapping("/foods")
    Mono<Response> getFoods();

    /**
     * Handles the incoming GET request "/foods/{id}"
     *
     * @param id of the food to be retrieved
     * @return food
     *
     * @see com.tascigorkem.restaurantservice.api.food.FoodControllerResponseDto
     */
    @GetMapping("/foods/{id}")
    Mono<Response> getFoodById(@PathVariable("id") UUID id);

    /**
     * Handles the incoming POST request "/foods"
     *
     * @param foodControllerRequestDto fields of food to be added
     * @return added food
     *
     * @see com.tascigorkem.restaurantservice.api.food.FoodControllerResponseDto
     */
    @PostMapping("/foods")
    Mono<Response> addFood(@RequestBody FoodControllerRequestDto foodControllerRequestDto);

    /**
     * Handles the incoming PUT request "/foods/{id}"
     *
     * @param id of the food to be updated
     * @param foodControllerRequestDto fields of food to be updated
     * @return updated food
     *
     * @see com.tascigorkem.restaurantservice.api.food.FoodControllerResponseDto
     */
    @PutMapping("/foods/{id}")
    Mono<Response> updateFood(@PathVariable("id") UUID id, @RequestBody FoodControllerRequestDto foodControllerRequestDto);

    /**
     * Handles the incoming DELETE request "/foods/{id}"
     *
     * @param id of the food to be deleted
     * @return removed food
     *
     * @see com.tascigorkem.restaurantservice.api.food.FoodControllerResponseDto
     */
    @DeleteMapping("/foods/{id}")
    Mono<Response> removeFood(@PathVariable("id") UUID id);

    Function<FoodDto, FoodControllerResponseDto> mapToFoodControllerResponseDto();

    Function<FoodControllerRequestDto, FoodDto> mapToFoodDto();
}
