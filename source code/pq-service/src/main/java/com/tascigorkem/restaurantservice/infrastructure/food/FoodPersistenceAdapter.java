package com.tascigorkem.restaurantservice.infrastructure.food;

import com.tascigorkem.restaurantservice.domain.exception.FoodNotFoundException;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.food.FoodPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class FoodPersistenceAdapter implements FoodPersistencePort {

    private final FoodRepository foodRepository;

    public FoodPersistenceAdapter(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Flux<FoodDto> getAllFoods() {
        return foodRepository.findAll().filter(foodEntity -> !foodEntity.isDeleted())
                .map(this::mapToFoodDto);
    }

    @Override
    public Mono<FoodDto> getFoodById(UUID id) {
        return foodRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new FoodNotFoundException("id", id.toString())))
                .map(this::mapToFoodDto);
    }

    @Override
    public Mono<FoodDto> addFood(FoodDto foodDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());
        return foodRepository.save(FoodEntity.builder()
                .id(UUID.randomUUID())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(foodDto.getName())
                .vegetable(foodDto.isVegetable())
                .price(foodDto.getPrice())
                .imageUrl(foodDto.getImageUrl())
                .build())
                .map(this::mapToFoodDto);
    }

    @Override
    public Mono<FoodDto> updateFood(FoodDto foodDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return foodRepository.findById(foodDto.getId()).flatMap(foodEntity -> {
            foodEntity.setUpdateTime(now);
            foodEntity.setStatus(Status.UPDATED);
            foodEntity.setName(foodDto.getName());
            foodEntity.setVegetable(foodDto.isVegetable());
            foodEntity.setPrice(foodDto.getPrice());
            foodEntity.setImageUrl(foodDto.getImageUrl());
            return foodRepository.save(foodEntity);
        })
                .switchIfEmpty(
                        Mono.error(new FoodNotFoundException("id", foodDto.getId().toString())))
                .map(this::mapToFoodDto);
    }

    @Override
    public Mono<FoodDto> removeFood(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return foodRepository.findById(id).flatMap(foodEntity -> {
            foodEntity.setUpdateTime(now);
            foodEntity.setStatus(Status.UPDATED);
            foodEntity.setDeleted(true);
            foodEntity.setDeletionTime(now);
            return foodRepository.save(foodEntity);
        })
                .switchIfEmpty(
                        Mono.error(new FoodNotFoundException("id", id.toString())))
                .map(this::mapToFoodDto);

    }

    protected FoodDto mapToFoodDto(FoodEntity foodEntity) {
        return FoodDto.builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .vegetable(foodEntity.isVegetable())
                .price(foodEntity.getPrice())
                .imageUrl(foodEntity.getImageUrl())
                .build();
    }

    protected FoodEntity mapToFoodEntity(FoodDto foodDto) {
        return FoodEntity.builder()
                .id(foodDto.getId())
                .name(foodDto.getName())
                .vegetable(foodDto.isVegetable())
                .price(foodDto.getPrice())
                .imageUrl(foodDto.getImageUrl())
                .build();
    }
}
