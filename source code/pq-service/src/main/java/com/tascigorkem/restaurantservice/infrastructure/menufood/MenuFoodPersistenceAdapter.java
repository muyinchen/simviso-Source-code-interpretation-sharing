package com.tascigorkem.restaurantservice.infrastructure.menufood;

import com.tascigorkem.restaurantservice.domain.exception.MenuFoodNotFoundException;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class MenuFoodPersistenceAdapter implements MenuFoodPersistencePort {

    private final FoodRepository foodRepository;
    private final MenuFoodRepository menuFoodRepository;

    public MenuFoodPersistenceAdapter(FoodRepository foodRepository, MenuFoodRepository menuFoodRepository) {
        this.foodRepository = foodRepository;
        this.menuFoodRepository = menuFoodRepository;
    }

    @Override
    public Flux<MenuFoodDto> getAllMenuFoods() {

        return menuFoodRepository.findAll().filter(menuFoodEntity -> !menuFoodEntity.isDeleted()).flatMap(menuFoodEntity ->
                foodRepository.findById(menuFoodEntity.getFoodId()).flatMap(foodEntity -> {
                    MenuFoodDto menuFoodDto = this.mapToMenuFoodDto(menuFoodEntity);
                    menuFoodDto.setOriginalPrice(foodEntity.getPrice());
                    menuFoodDto.setFoodName(foodEntity.getName());
                    return Mono.just(menuFoodDto);
                }));
    }


    @Override
    public Mono<MenuFoodDto> addMenuFood(MenuFoodDto menuFoodDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());
        return menuFoodRepository.save(MenuFoodEntity.builder()
                .id(UUID.randomUUID())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .menuId(menuFoodDto.getMenuId())
                .foodId(menuFoodDto.getFoodId())
                .extended(menuFoodDto.isExtended())
                .extendedPrice(menuFoodDto.getExtendedPrice())
                .build())
                .map(this::mapToMenuFoodDto);
    }

    @Override
    public Mono<MenuFoodDto> getFoodPriceInfoByMenuId(UUID menuId, UUID foodId) {
        return menuFoodRepository.findAllByMenuIdAndFoodId(menuId, foodId).flatMap(menuFoodEntity ->
                foodRepository.findById(foodId).flatMap(foodEntity -> {
                    MenuFoodDto menuFoodDto = this.mapToMenuFoodDto(menuFoodEntity);
                    menuFoodDto.setOriginalPrice(foodEntity.getPrice());
                    menuFoodDto.setFoodName(foodEntity.getName());
                    return Mono.just(menuFoodDto);
                }))
                .switchIfEmpty(
                        Mono.error(new MenuFoodNotFoundException("menu id - food id", menuId.toString() + " " + foodId.toString())));
    }

    protected MenuFoodDto mapToMenuFoodDto(MenuFoodEntity menuFoodEntity) {
        return MenuFoodDto.builder()
                .id(menuFoodEntity.getId())
                .menuId(menuFoodEntity.getMenuId())
                .foodId(menuFoodEntity.getFoodId())
                .extended(menuFoodEntity.isExtended())
                .extendedPrice(menuFoodEntity.getExtendedPrice())
                .build();
    }

    public MenuFoodEntity mapToMenuFoodEntity(MenuFoodDto menuFoodDto) {
        return MenuFoodEntity.builder()
                .id(menuFoodDto.getId())
                .extended(menuFoodDto.isExtended())
                .extendedPrice(menuFoodDto.getExtendedPrice())
                .menuId(menuFoodDto.getMenuId())
                .foodId(menuFoodDto.getFoodId())
                .build();
    }
}
