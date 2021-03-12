package com.tascigorkem.restaurantservice.infrastructure.menu;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MenuPersistenceAdapterTest {

    private final MenuRepository menuRepository = mock(MenuRepository.class);
    private final MenuPersistenceAdapter subject = new MenuPersistenceAdapter(menuRepository);

    /**
     * Unit test for MenuPersistenceAdapter:getAllMenus
     */
    @Test
    void testGetAllMenus() {
        // arrange
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(DomainModelFaker.fakeId());
        List<MenuDto> menuDtoList = Arrays.asList(fakeMenuDto, fakeMenuDto, fakeMenuDto);
        List<MenuEntity> menuEntityList = menuDtoList.stream().map(subject::mapToMenuEntity).collect(Collectors.toList());
        when(menuRepository.findAll()).thenReturn(Flux.fromIterable(menuEntityList));

        // act
        Flux<MenuDto> result = subject.getAllMenus();

        //assert
        StepVerifier.create(result)
                .expectNext(menuDtoList.get(0))
                .expectNext(menuDtoList.get(1))
                .expectNext(menuDtoList.get(2))
                .expectComplete()
                .verify();

        verify(menuRepository).findAll();
    }

    /**
     * Unit test for MenuPersistenceAdapter:getMenuById
     */
    @Test
    void getMenuById() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        MenuEntity fakeMenuEntity = subject.mapToMenuEntity(fakeMenuDto);
        when(menuRepository.findById(fakeMenuId)).thenReturn(Mono.just(fakeMenuEntity));

        // act
        Mono<MenuDto> result = subject.getMenuById(fakeMenuId);

        // assert
        StepVerifier.create(result)
                .assertNext(menuEntity ->
                        assertThat(menuEntity)
                                .usingRecursiveComparison()
                                .isEqualTo(fakeMenuDto))
                .verifyComplete();
    }

    /**
     * Unit test for MenuPersistenceAdapter:addMenu
     */
    @Test
    void givenMenuControllerRequestDto_whenCreateMenu_thenReturnSuccessful_andReturnMenu() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        MenuEntity fakeMenuEntity = subject.mapToMenuEntity(fakeMenuDto);
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(Mono.just(fakeMenuEntity));

        // act
        Mono<MenuDto> result = subject.addMenu(fakeMenuDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuRepository).save(any(MenuEntity.class));
    }

    /**
     * Unit test for MenuPersistenceAdapter:updateMenu
     */
    @Test
    void givenMenuId_andMenuDto_whenUpdateMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        MenuEntity fakeMenuEntity = subject.mapToMenuEntity(fakeMenuDto);
        when(menuRepository.findById(fakeMenuId)).thenReturn(Mono.just(fakeMenuEntity));
        when(menuRepository.save(fakeMenuEntity)).thenReturn(Mono.just(fakeMenuEntity));

        // act

        Mono<MenuDto> result = subject.updateMenu(fakeMenuDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuRepository).findById(fakeMenuId);
        verify(menuRepository).save(any(MenuEntity.class));
    }

    /**
     * Unit test for MenuPersistenceAdapter:removeMenu
     */
    @Test
    void givenMenuId_whenRemoveMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        MenuEntity fakeMenuEntity = subject.mapToMenuEntity(fakeMenuDto);
        when(menuRepository.findById(fakeMenuId)).thenReturn(Mono.just(fakeMenuEntity));
        when(menuRepository.save(fakeMenuEntity)).thenReturn(Mono.just(fakeMenuEntity));

        // act
        Mono<MenuDto> result = subject.removeMenu(fakeMenuId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeMenuDto)
                .verifyComplete();

        verify(menuRepository).findById(fakeMenuId);
        verify(menuRepository).save(any(MenuEntity.class));
    }
}