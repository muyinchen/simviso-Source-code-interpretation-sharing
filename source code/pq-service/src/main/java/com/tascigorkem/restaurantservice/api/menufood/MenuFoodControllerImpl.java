package com.tascigorkem.restaurantservice.api.menufood;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class MenuFoodControllerImpl implements MenuFoodController {

    private final MenuFoodService menuFoodService;

    public MenuFoodControllerImpl(MenuFoodService menuFoodService) {
        this.menuFoodService = menuFoodService;
    }

    @Override
    public Mono<Response> getAllMenuFoods() {
        return menuFoodService.getAllMenuFoods()
                .map(mapToMenuFoodControllerResponseDto())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addMenuFood(UUID menuId, UUID foodId, MenuFoodControllerRequestDto menuFoodControllerRequestDto) {
        MenuFoodDto menuFoodDto = mapToMenuFoodDto().apply(menuFoodControllerRequestDto);
        menuFoodDto.setMenuId(menuId);
        menuFoodDto.setFoodId(foodId);
        return menuFoodService.addMenuFood(menuFoodDto)
                .map(mapToMenuFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getFoodPriceInfoByMenuId(UUID menuId, UUID foodId) {
        return menuFoodService.getFoodPriceInfoByMenuId(menuId, foodId)
                .map(mapToMenuFoodControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Function<MenuFoodDto, MenuFoodControllerResponseDto> mapToMenuFoodControllerResponseDto() {
        return menuFoodDto ->
                MenuFoodControllerResponseDto.builder()
                        .id(menuFoodDto.getId())
                        .menuId(menuFoodDto.getMenuId())
                        .foodId(menuFoodDto.getFoodId())
                        .foodName(menuFoodDto.getFoodName())
                        .originalPrice(menuFoodDto.getOriginalPrice())
                        .extended(menuFoodDto.isExtended())
                        .extendedPrice(menuFoodDto.getExtendedPrice())
                        .build();
    }

    @Override
    public Function<MenuFoodControllerRequestDto, MenuFoodDto> mapToMenuFoodDto() {
        return menuFoodControllerRequestDto ->
                MenuFoodDto.builder()
                        .extended(menuFoodControllerRequestDto.isExtended())
                        .extendedPrice(menuFoodControllerRequestDto.getExtendedPrice())
                        .build();
    }
}
