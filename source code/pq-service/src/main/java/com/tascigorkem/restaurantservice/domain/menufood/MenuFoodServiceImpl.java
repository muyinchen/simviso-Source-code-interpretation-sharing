package com.tascigorkem.restaurantservice.domain.menufood;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class MenuFoodServiceImpl implements MenuFoodService {


    private final MenuFoodPersistencePort menuFoodPersistencePort;

    public MenuFoodServiceImpl(MenuFoodPersistencePort menuFoodPersistencePort) {
        this.menuFoodPersistencePort = menuFoodPersistencePort;
    }

    @Override
    public Flux<MenuFoodDto> getAllMenuFoods() {
        return menuFoodPersistencePort.getAllMenuFoods();
    }

    @Override
    public Mono<MenuFoodDto> addMenuFood(MenuFoodDto menuFoodDto) {
        return menuFoodPersistencePort.addMenuFood(menuFoodDto);
    }

    @Override
    public Mono<MenuFoodDto> getFoodPriceInfoByMenuId(UUID menuId, UUID foodId) {
        return menuFoodPersistencePort.getFoodPriceInfoByMenuId(menuId, foodId);
    }
}
