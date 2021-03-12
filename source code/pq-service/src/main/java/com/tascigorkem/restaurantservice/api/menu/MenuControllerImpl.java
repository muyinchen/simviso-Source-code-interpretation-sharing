package com.tascigorkem.restaurantservice.api.menu;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class MenuControllerImpl implements MenuController {

    private final MenuService menuService;

    public MenuControllerImpl(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public Mono<Response> getMenus() {
        return menuService.getAllMenus()
                .map(mapToMenuControllerResponseDto())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getMenuById(UUID id) {
        return menuService.getMenuById(id)
                .map(mapToMenuControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addMenu(MenuControllerRequestDto menuControllerRequestDto) {
        return menuService.addMenu(mapToMenuDto().apply(menuControllerRequestDto))
                .map(mapToMenuControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> updateMenu(UUID id, MenuControllerRequestDto menuControllerRequestDto) {
        MenuDto menuDto = mapToMenuDto().apply(menuControllerRequestDto);
        menuDto.setId(id);
        return menuService.updateMenu(menuDto)
                .map(mapToMenuControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> removeMenu(UUID id) {
        return menuService.removeMenu(id)
                .map(mapToMenuControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Function<MenuDto, MenuControllerResponseDto> mapToMenuControllerResponseDto() {
        return menuDto ->
                MenuControllerResponseDto.builder()
                        .id(menuDto.getId())
                        .name(menuDto.getName())
                        .menuType(menuDto.getMenuType())
                        .restaurantId(menuDto.getRestaurantId())
                        .build();
    }

    @Override
    public Function<MenuControllerRequestDto, MenuDto> mapToMenuDto() {
        return menuControllerRequestDto ->
                MenuDto.builder()
                        .name(menuControllerRequestDto.getName())
                        .menuType(menuControllerRequestDto.getMenuType())
                        .restaurantId(menuControllerRequestDto.getRestaurantId())
                        .build();
    }
}
