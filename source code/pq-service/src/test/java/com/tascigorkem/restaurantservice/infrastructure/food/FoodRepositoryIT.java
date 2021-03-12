package com.tascigorkem.restaurantservice.infrastructure.food;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FoodRepositoryIT {

    @Autowired
    private FoodRepository foodRepository;

    @Test
    void testGetAllFoods() {
        // arrange
        UUID fakeFoodId1 = DomainModelFaker.fakeId();
        UUID fakeFoodId2 = DomainModelFaker.fakeId();
        UUID fakeFoodId3 = DomainModelFaker.fakeId();

        FoodDto fakeFoodDto1 = DomainModelFaker.getFakeFoodDto(fakeFoodId1);
        FoodDto fakeFoodDto2 = DomainModelFaker.getFakeFoodDto(fakeFoodId2);
        FoodDto fakeFoodDto3 = DomainModelFaker.getFakeFoodDto(fakeFoodId3);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        FoodEntity foodEntity1 = FoodEntity.builder()
                .id(fakeFoodId1)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto1.getName())
                .vegetable(fakeFoodDto1.isVegetable())
                .price(fakeFoodDto1.getPrice())
                .imageUrl(fakeFoodDto1.getImageUrl())
                .build();
        FoodEntity foodEntity2 = FoodEntity.builder()
                .id(fakeFoodId2)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto2.getName())
                .vegetable(fakeFoodDto2.isVegetable())
                .price(fakeFoodDto2.getPrice())
                .imageUrl(fakeFoodDto2.getImageUrl())
                .build();
        FoodEntity foodEntity3 = FoodEntity.builder()
                .id(fakeFoodId3)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto3.getName())
                .vegetable(fakeFoodDto3.isVegetable())
                .price(fakeFoodDto3.getPrice())
                .imageUrl(fakeFoodDto3.getImageUrl())
                .build();

        // prepare db, delete all entities and insert three foods
        foodRepository.deleteAll()
                .then(foodRepository.save(foodEntity1))
                .then(foodRepository.save(foodEntity2))
                .then(foodRepository.save(foodEntity3))
                .block();

        // act
        Flux<FoodEntity> result = foodRepository.findAll();
        List<FoodEntity> resultFoodEntityList = result.collectList().block();

        // assert
        assertNotNull(resultFoodEntityList);
        assertEquals(resultFoodEntityList.size(), 3);

        FoodEntity resultFoodEntity1 = resultFoodEntityList.stream()
                .filter(companyEntityItem -> fakeFoodDto1.getId().equals(companyEntityItem.getId())).findFirst().get();

        FoodEntity resultFoodEntity2 = resultFoodEntityList.stream()
                .filter(companyEntityItem -> fakeFoodDto2.getId().equals(companyEntityItem.getId())).findFirst().get();

        FoodEntity resultFoodEntity3 = resultFoodEntityList.stream()
                .filter(companyEntityItem -> fakeFoodDto3.getId().equals(companyEntityItem.getId())).findFirst().get();

        assertAll(
                () -> assertEquals(fakeFoodDto1.getId(), resultFoodEntity1.getId()),
                () -> assertEquals(fakeFoodDto1.getName(), resultFoodEntity1.getName()),
                () -> assertEquals(fakeFoodDto1.isVegetable(), resultFoodEntity1.isVegetable()),

                () -> assertEquals(fakeFoodDto2.getId(), resultFoodEntity2.getId()),
                () -> assertEquals(fakeFoodDto2.getName(), resultFoodEntity2.getName()),
                () -> assertEquals(fakeFoodDto2.isVegetable(), resultFoodEntity2.isVegetable()),

                () -> assertEquals(fakeFoodDto3.getId(), resultFoodEntity3.getId()),
                () -> assertEquals(fakeFoodDto3.getName(), resultFoodEntity3.getName()),
                () -> assertEquals(fakeFoodDto3.isVegetable(), resultFoodEntity3.isVegetable())
        );

    }

    @Test
    void findById() {
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

        // act
        Mono<FoodEntity> result = foodRepository.findById(fakeFoodId);
        FoodEntity foodEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(fakeFoodDto.getId(), foodEntity.getId()),
                () -> assertEquals(fakeFoodDto.getName(), foodEntity.getName()),
                () -> assertEquals(fakeFoodDto.isVegetable(), foodEntity.isVegetable()),
                () -> assertEquals(fakeFoodDto.getPrice(), foodEntity.getPrice()),
                () -> assertEquals(fakeFoodDto.getImageUrl(), foodEntity.getImageUrl())
        );
    }

    @Test
    void givenFoodId_whenCreateFoodTwiceWithSameId_thenFails() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, insert first entity
        foodRepository.deleteAll().then(
                foodRepository.save(FoodEntity.builder()
                        .id(fakeFoodId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeFoodDto.getName())
                        .vegetable(fakeFoodDto.isVegetable())
                        .price(fakeFoodDto.getPrice())
                        .imageUrl(fakeFoodDto.getImageUrl())
                        .build())
        ).block();

        // act
        // insert second entity
        Executable createFood = () ->
                foodRepository.save(FoodEntity.builder()
                        .id(fakeFoodId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeFoodDto.getName())
                        .vegetable(fakeFoodDto.isVegetable())
                        .price(fakeFoodDto.getPrice())
                        .imageUrl(fakeFoodDto.getImageUrl())
                        .build()).block();

        // assert
        assertThrows(DataIntegrityViolationException.class, createFood);

    }

    @Test
    void testFoodFields() {
        // arrange
        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, insert first entity
        FoodEntity insertedFood = FoodEntity.builder()
                .id(fakeFoodId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto.getName())
                .vegetable(fakeFoodDto.isVegetable())
                .price(fakeFoodDto.getPrice())
                .imageUrl(fakeFoodDto.getImageUrl())
                .build();

        foodRepository.deleteAll()
                .then(foodRepository.save(insertedFood))
                .block();

        // act
        Mono<FoodEntity> result = foodRepository.findById(fakeFoodId);
        FoodEntity foodEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(insertedFood.getId(), foodEntity.getId()),
                () -> assertEquals(insertedFood.getCreationTime(), foodEntity.getCreationTime()),
                () -> assertEquals(insertedFood.getUpdateTime(), foodEntity.getUpdateTime()),
                () -> assertEquals(insertedFood.getStatus(), foodEntity.getStatus()),
                () -> assertEquals(insertedFood.getDeletionTime(), foodEntity.getDeletionTime()),
                () -> assertEquals(insertedFood.isDeleted(), foodEntity.isDeleted()),
                () -> assertEquals(insertedFood.getName(), foodEntity.getName()),
                () -> assertEquals(insertedFood.isVegetable(), foodEntity.isVegetable()),
                () -> assertEquals(insertedFood.getPrice(), foodEntity.getPrice()),
                () -> assertEquals(insertedFood.getImageUrl(), foodEntity.getImageUrl())
        );
    }

}
