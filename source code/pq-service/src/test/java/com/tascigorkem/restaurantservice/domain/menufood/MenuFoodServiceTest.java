package com.tascigorkem.restaurantservice.domain.menufood;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class MenuFoodServiceTest {

    private final MenuFoodPersistencePort menuFoodPersistencePort = mock(MenuFoodPersistencePort.class);
    private final MenuFoodService subject = new MenuFoodServiceImpl(menuFoodPersistencePort);

    /**
     * Unit test for MenuFoodService:getAllMenuFoods
     */
    @Test
    void testGetAllMenuFoods() {
        // arrange
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(DomainModelFaker.fakeId());
        List<MenuFoodDto> menuFoodDtoList = Arrays.asList(fakeMenuFoodDto, fakeMenuFoodDto, fakeMenuFoodDto);
        when(menuFoodPersistencePort.getAllMenuFoods()).thenReturn(Flux.fromIterable(menuFoodDtoList));

        // act
        Flux<MenuFoodDto> result = subject.getAllMenuFoods();

        //assert
        StepVerifier.create(result)
                .expectNext(menuFoodDtoList.get(0))
                .expectNext(menuFoodDtoList.get(1))
                .expectNext(menuFoodDtoList.get(2))
                .expectComplete()
                .verify();

        verify(menuFoodPersistencePort).getAllMenuFoods();
    }

    /**
     * Unit test for MenuFoodService:getFoodPriceInfoByMenuId
     */
    @Test
    void testGetFoodPriceInfoByMenuId() {
        // arrange
        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(fakeMenuFoodId);
        when(menuFoodPersistencePort.getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId()))
                .thenReturn(Mono.just(fakeMenuFoodDto));

        // act
        Mono<MenuFoodDto> result = subject.getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId());

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuFoodDto)
                .expectComplete()
                .verify();

        verify(menuFoodPersistencePort).getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId());
    }

    /**
     * Unit test for MenuFoodService:addMenuFood
     */
    @Test
    void givenMenuFoodControllerRequestDto_whenCreateMenuFood_thenReturnSuccessful_andReturnMenuFood() {
        // arrange
        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(fakeMenuFoodId);
        fakeMenuFoodDto.setFoodName(null);
        fakeMenuFoodDto.setOriginalPrice(null);
        when(menuFoodPersistencePort.addMenuFood(fakeMenuFoodDto)).thenReturn(Mono.just(fakeMenuFoodDto));

        // act
        Mono<MenuFoodDto> result = subject.addMenuFood(fakeMenuFoodDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuFoodDto)
                .verifyComplete();

        verify(menuFoodPersistencePort).addMenuFood(fakeMenuFoodDto);
    }

}
