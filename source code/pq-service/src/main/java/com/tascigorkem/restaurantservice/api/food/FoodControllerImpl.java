package com.tascigorkem.restaurantservice.api.food;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.food.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class FoodControllerImpl implements FoodController {

    private final FoodService foodService;

    public FoodControllerImpl(FoodService foodService) {
        this.foodService = foodService;
    }

    @Override
    public Mono<Response> getFoods() {
        return foodService.getAllFoods()
                .map(mapToFoodControllerResponseDto())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getFoodById(UUID id) {
        return foodService.getFoodById(id)
                .map(mapToFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addFood(FoodControllerRequestDto foodControllerRequestDto) {
        return foodService.addFood(mapToFoodDto().apply(foodControllerRequestDto))
                .map(mapToFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> updateFood(UUID id, FoodControllerRequestDto foodControllerRequestDto) {
        FoodDto foodDto = mapToFoodDto().apply(foodControllerRequestDto);
        foodDto.setId(id);
        return foodService.updateFood(foodDto)
                .map(mapToFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> removeFood(UUID id) {
        return foodService.removeFood(id)
                .map(mapToFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Function<FoodDto, FoodControllerResponseDto> mapToFoodControllerResponseDto() {
        return foodDto ->
                FoodControllerResponseDto.builder()
                        .id(foodDto.getId())
                        .name(foodDto.getName())
                        .vegetable(foodDto.isVegetable())
                        .price(foodDto.getPrice())
                        .imageUrl(foodDto.getImageUrl())
                        .build();
    }

    @Override
    public Function<FoodControllerRequestDto, FoodDto> mapToFoodDto() {
        return foodControllerRequestDto ->
                FoodDto.builder()
                        .name(foodControllerRequestDto.getName())
                        .vegetable(foodControllerRequestDto.isVegetable())
                        .price(foodControllerRequestDto.getPrice())
                        .imageUrl(foodControllerRequestDto.getImageUrl())
                        .build();
    }
}
