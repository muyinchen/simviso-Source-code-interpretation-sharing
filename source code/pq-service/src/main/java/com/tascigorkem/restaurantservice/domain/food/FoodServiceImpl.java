package com.tascigorkem.restaurantservice.domain.food;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodPersistencePort foodPersistencePort;

    public FoodServiceImpl(FoodPersistencePort foodPersistencePort) {
        this.foodPersistencePort = foodPersistencePort;
    }

    @Override
    public Flux<FoodDto> getAllFoods() {
        return foodPersistencePort.getAllFoods();
    }

    @Override
    public Mono<FoodDto> getFoodById(UUID id) {
        return foodPersistencePort.getFoodById(id);
    }

    @Override
    public Mono<FoodDto> addFood(FoodDto foodDto) {
        return foodPersistencePort.addFood(foodDto);
    }

    @Override
    public Mono<FoodDto> updateFood(FoodDto foodDto) {
        return foodPersistencePort.updateFood(foodDto);
    }

    @Override
    public Mono<FoodDto> removeFood(UUID id) {
        return foodPersistencePort.removeFood(id);
    }

}
