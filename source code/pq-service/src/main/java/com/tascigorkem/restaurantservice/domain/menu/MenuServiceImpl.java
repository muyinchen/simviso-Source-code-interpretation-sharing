package com.tascigorkem.restaurantservice.domain.menu;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuPersistencePort menuPersistencePort;

    public MenuServiceImpl(MenuPersistencePort menuPersistencePort) {
        this.menuPersistencePort = menuPersistencePort;
    }

    @Override
    public Flux<MenuDto> getAllMenus() {
        return menuPersistencePort.getAllMenus();
    }

    @Override
    public Mono<MenuDto> getMenuById(UUID id) {
        return menuPersistencePort.getMenuById(id);
    }

    @Override
    public Mono<MenuDto> addMenu(MenuDto menuDto) {
        return menuPersistencePort.addMenu(menuDto);
    }

    @Override
    public Mono<MenuDto> updateMenu(MenuDto menuDto) {
        return menuPersistencePort.updateMenu(menuDto);
    }

    @Override
    public Mono<MenuDto> removeMenu(UUID id) {
        return menuPersistencePort.removeMenu(id);
    }

}
