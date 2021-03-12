package com.tascigorkem.restaurantservice.api.menu;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface MenuController {

    /**
     * Handles the incoming GET request "/menus"
     *
     * @return retrieve all non-deleted menus
     *
     * @see com.tascigorkem.restaurantservice.api.menu.MenuControllerResponseDto
     */
    @GetMapping("/menus")
    Mono<Response> getMenus();

    /**
     * Handles the incoming GET request "/menus/{id}"
     *
     * @param id of the menu to be retrieved
     * @return menu
     *
     * @see com.tascigorkem.restaurantservice.api.menu.MenuControllerResponseDto
     */
    @GetMapping("/menus/{id}")
    Mono<Response> getMenuById(@PathVariable("id") UUID id);

    /**
     * Handles the incoming POST request "/menus"
     *
     * @param menuControllerRequestDto fields of menu to be added
     * @return added menu
     *
     * @see com.tascigorkem.restaurantservice.api.menu.MenuControllerResponseDto
     */
    @PostMapping("/menus")
    Mono<Response> addMenu(@RequestBody MenuControllerRequestDto menuControllerRequestDto);

    /**
     * Handles the incoming PUT request "/menus/{id}"
     *
     * @param id of the menu to be updated
     * @param menuControllerRequestDto fields of menu to be updated
     * @return updated menu
     *
     * @see com.tascigorkem.restaurantservice.api.menu.MenuControllerResponseDto
     */
    @PutMapping("/menus/{id}")
    Mono<Response> updateMenu(@PathVariable("id") UUID id, @RequestBody MenuControllerRequestDto menuControllerRequestDto);

    /**
     * Handles the incoming DELETE request "/menus/{id}"
     *
     * @param id of the menu to be deleted
     * @return removed menu
     *
     * @see com.tascigorkem.restaurantservice.api.menu.MenuControllerResponseDto
     */
    @DeleteMapping("/menus/{id}")
    Mono<Response> removeMenu(@PathVariable("id") UUID id);

    Function<MenuDto, MenuControllerResponseDto> mapToMenuControllerResponseDto();

    Function<MenuControllerRequestDto, MenuDto> mapToMenuDto();
}
