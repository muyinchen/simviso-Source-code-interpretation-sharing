package com.tascigorkem.restaurantservice.domain.menu;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private final MenuPersistencePort menuPersistencePort = mock(MenuPersistencePort.class);
    private final MenuService subject = new MenuServiceImpl(menuPersistencePort);

    /**
     * Unit test for MenuService:getAllMenus
     */
    @Test
    void testGetAllMenus() {
        // arrange
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(DomainModelFaker.fakeId());
        List<MenuDto> menuDtoList = Arrays.asList(fakeMenuDto, fakeMenuDto, fakeMenuDto);
        when(menuPersistencePort.getAllMenus()).thenReturn(Flux.fromIterable(menuDtoList));

        // act
        Flux<MenuDto> result = subject.getAllMenus();

        //assert
        StepVerifier.create(result)
                .expectNext(menuDtoList.get(0))
                .expectNext(menuDtoList.get(1))
                .expectNext(menuDtoList.get(2))
                .expectComplete()
                .verify();

        verify(menuPersistencePort).getAllMenus();
    }

    /**
     * Unit test for MenuService:getMenuById
     */
    @Test
    void givenMenuId_whenGetMenu_andMenuExists_thenReturnMenu() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuPersistencePort.getMenuById(fakeMenuId)).thenReturn(Mono.just(fakeMenuDto));

        // act
        Mono<MenuDto> result = subject.getMenuById(fakeMenuId);

        // assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuPersistencePort).getMenuById(fakeMenuId);

    }

    /**
     * Unit test for MenuService:addMenu
     */
    @Test
    void givenMenuControllerRequestDto_whenCreateMenu_thenReturnSuccessful_andReturnMenu() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuPersistencePort.addMenu(fakeMenuDto)).thenReturn(Mono.just(fakeMenuDto));

        // act
        Mono<MenuDto> result = subject.addMenu(fakeMenuDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuPersistencePort).addMenu(fakeMenuDto);
    }


    /**
     * Unit test for MenuService:updateMenu
     */
    @Test
    void givenMenuId_andMenuDto_whenUpdateMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuPersistencePort.updateMenu(fakeMenuDto)).thenReturn(Mono.just(fakeMenuDto));

        // act
        Mono<MenuDto> result = subject.updateMenu(fakeMenuDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuPersistencePort).updateMenu(fakeMenuDto);
    }

    /**
     * Unit test for MenuService:removeMenu
     */
    @Test
    void givenMenuId_whenRemoveMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuPersistencePort.removeMenu(fakeMenuId)).thenReturn(Mono.just(fakeMenuDto));

        // act
        Mono<MenuDto> result = subject.removeMenu(fakeMenuId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuPersistencePort).removeMenu(fakeMenuId);
    }
}