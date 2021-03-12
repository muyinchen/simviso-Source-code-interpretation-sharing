package com.tascigorkem.restaurantservice.api.menufood;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface MenuFoodController {

    /**
     * Handles the incoming GET request "/menus/foods"
     *
     * @return retrieve all non-deleted foods with related menu ids
     * @see com.tascigorkem.restaurantservice.api.menufood.MenuFoodControllerResponseDto
     */
    @GetMapping("/menus/foods")
    Mono<Response> getAllMenuFoods();

    /**
     * Handles the incoming POST request "/companies"
     *
     * @param menuId                       of the menu to be added
     * @param foodId                       of the food to be added
     * @param menuFoodControllerRequestDto fields of menu-food to be added
     * @return added company
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @PostMapping("/menus/{menuId}/foods/{foodId}")
    Mono<Response> addMenuFood(@PathVariable("menuId") UUID menuId, @PathVariable("foodId") UUID foodId,
                               @RequestBody MenuFoodControllerRequestDto menuFoodControllerRequestDto);

    /**
     * Handles the incoming GET request "/menus/{menuId}/foods/{foodId}"
     *
     * @param menuId of the menu to be retrieved
     * @param foodId of the food to be retrieved
     * @return retrieve food with menu id
     * @see com.tascigorkem.restaurantservice.api.menufood.MenuFoodControllerResponseDto
     */
    @GetMapping("/menus/{menuId}/foods/{foodId}")
    Mono<Response> getFoodPriceInfoByMenuId(@PathVariable("menuId") UUID menuId, @PathVariable("foodId") UUID foodId);

    Function<MenuFoodDto, MenuFoodControllerResponseDto> mapToMenuFoodControllerResponseDto();

    Function<MenuFoodControllerRequestDto, MenuFoodDto> mapToMenuFoodDto();

}
