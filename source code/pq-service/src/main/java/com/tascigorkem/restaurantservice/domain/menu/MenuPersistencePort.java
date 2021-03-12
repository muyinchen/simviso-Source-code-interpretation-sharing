package com.tascigorkem.restaurantservice.domain.menu;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MenuPersistencePort {

    Flux<MenuDto> getAllMenus();

    Mono<MenuDto> getMenuById(UUID id);

    Mono<MenuDto> addMenu(MenuDto menuDto);

    Mono<MenuDto> updateMenu(MenuDto fakeMenuDto);

    Mono<MenuDto> removeMenu(UUID id);
}
