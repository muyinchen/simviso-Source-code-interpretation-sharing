package com.tascigorkem.restaurantservice.infrastructure.menufood;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodEntity;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodRepository;
import com.tascigorkem.restaurantservice.infrastructure.menu.MenuEntity;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MenuFoodPersistenceAdapterTest {

    private final MenuFoodRepository menuFoodRepository = mock(MenuFoodRepository.class);
    private final FoodRepository foodRepository = mock(FoodRepository.class);
    private final MenuFoodPersistenceAdapter subject = new MenuFoodPersistenceAdapter(foodRepository, menuFoodRepository);

    /**
     * Unit test for MenuFoodPersistenceAdapter:getAllMenuFoods
     */
    @Test
    void testGetAllMenuFoods() {
        // arrange
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(DomainModelFaker.fakeId());
        List<MenuFoodDto> menuFoodDtoList = Arrays.asList(fakeMenuFoodDto, fakeMenuFoodDto, fakeMenuFoodDto);
        List<MenuFoodEntity> menuFoodEntityList = menuFoodDtoList.stream()
                .map(subject::mapToMenuFoodEntity).collect(Collectors.toList());

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        FoodEntity fakeFoodEntity = FoodEntity.builder()
                .id(fakeMenuFoodDto.getFoodId())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuFoodDto.getFoodName())
                .price(fakeMenuFoodDto.getOriginalPrice())
                .build();

        when(menuFoodRepository.findAll()).thenReturn(Flux.fromIterable(menuFoodEntityList));
        when(foodRepository.findById(fakeMenuFoodDto.getFoodId())).thenReturn(Mono.just(fakeFoodEntity));

        // act
        Flux<MenuFoodDto> result = subject.getAllMenuFoods();

        //assert
        StepVerifier.create(result)
                .expectNext(menuFoodDtoList.get(0))
                .expectNext(menuFoodDtoList.get(1))
                .expectNext(menuFoodDtoList.get(2))
                .expectComplete()
                .verify();

        verify(menuFoodRepository).findAll();
    }

    
    /**
     * Unit test for MenuFoodPersistenceAdapter:getMenuFoodById
     */
    @Test
    void getMenuFoodById() {
        // arrange
        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(fakeMenuFoodId);
        MenuFoodEntity fakeMenuFoodEntity = subject.mapToMenuFoodEntity(fakeMenuFoodDto);

        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeMenuFoodDto.getFoodId());

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        FoodEntity fakeFoodEntity = FoodEntity.builder()
                .id(fakeFoodDto.getId())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto.getName())
                .vegetable(fakeFoodDto.isVegetable())
                .price(fakeFoodDto.getPrice())
                .build();

        when(menuFoodRepository.findAllByMenuIdAndFoodId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId()))
                .thenReturn(Mono.just(fakeMenuFoodEntity));
        when(foodRepository.findById(fakeMenuFoodEntity.getFoodId())).thenReturn(Mono.just(fakeFoodEntity));

        // act
        Mono<MenuFoodDto> result = subject.getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId());
        MenuFoodDto menuFoodDtoResult = result.block();

        // assert

        // assert
        assertAll(
                () -> assertEquals(fakeMenuFoodDto.getId(), menuFoodDtoResult.getId()),
                () -> assertEquals(fakeMenuFoodDto.isExtended(), menuFoodDtoResult.isExtended()),
                () -> assertEquals(fakeMenuFoodDto.getExtendedPrice(), menuFoodDtoResult.getExtendedPrice()),
                () -> assertEquals(fakeMenuFoodDto.getMenuId(), menuFoodDtoResult.getMenuId()),
                () -> assertEquals(fakeMenuFoodDto.getFoodId(), menuFoodDtoResult.getFoodId()),
                () -> assertEquals(fakeFoodDto.getPrice(), menuFoodDtoResult.getOriginalPrice()),
                () -> assertEquals(fakeFoodDto.getName(), menuFoodDtoResult.getFoodName())
        );
    }


    /**
     * Unit test for MenuFoodPersistenceAdapter:addMenuFood
     */
    @Test
    void givenMenuFoodControllerRequestDto_whenCreateMenuFood_thenReturnSuccessful_andReturnMenuFood() {
        // arrange
        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(fakeMenuFoodId);
        fakeMenuFoodDto.setFoodName(null);
        fakeMenuFoodDto.setOriginalPrice(null);
        MenuFoodEntity fakeMenuFoodEntity = subject.mapToMenuFoodEntity(fakeMenuFoodDto);
        when(menuFoodRepository.save(any(MenuFoodEntity.class))).thenReturn(Mono.just(fakeMenuFoodEntity));

        // act
        Mono<MenuFoodDto> result = subject.addMenuFood(fakeMenuFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuFoodDto)
                .verifyComplete();

        verify(menuFoodRepository).save(any(MenuFoodEntity.class));
    }

}