package com.tascigorkem.restaurantservice.domain.food;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodEntity;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodPersistenceAdapter;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
class FoodServiceIT {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodPersistencePort foodPersistencePort;

    @Test
    void getFoodById() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one entity
        foodRepository.deleteAll()
                .then(foodRepository.save(FoodEntity.builder()
                        .id(fakeFoodId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeFoodDto.getName())
                        .vegetable(fakeFoodDto.isVegetable())
                        .price(fakeFoodDto.getPrice())
                        .imageUrl(fakeFoodDto.getImageUrl())
                        .build()))
                .block();

        foodPersistencePort = new FoodPersistenceAdapter(foodRepository);

        // act
        Mono<FoodDto> result = foodPersistencePort.getFoodById(fakeFoodId);

        // assert

        // option 1
        StepVerifier.create(result)
                .expectNext(fakeFoodDto)
                .verifyComplete();

        // option 2
//        assertAll(
//                () -> assertEquals(fakeFoodDto.getId(), foodDto.getId()),
//                () -> assertEquals(fakeFoodDto.getName(), foodDto.getName()),
//        );
    }
}
