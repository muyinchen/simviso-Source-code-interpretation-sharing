package com.tascigorkem.restaurantservice.api.restaurant;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantControllerImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public Mono<Response> getRestaurants() {
        return restaurantService.getAllRestaurants()
                .map(mapToRestaurantControllerResponseDto())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getRestaurantById(UUID id) {
        return restaurantService.getRestaurantById(id)
                .map(mapToRestaurantControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addRestaurant(RestaurantControllerRequestDto restaurantControllerRequestDto) {
        return restaurantService.addRestaurant(mapToRestaurantDto().apply(restaurantControllerRequestDto))
                .map(mapToRestaurantControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> updateRestaurant(UUID id, RestaurantControllerRequestDto restaurantControllerRequestDto) {
        RestaurantDto restaurantDto = mapToRestaurantDto().apply(restaurantControllerRequestDto);
        restaurantDto.setId(id);
        return restaurantService.updateRestaurant(restaurantDto)
                .map(mapToRestaurantControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> removeRestaurant(UUID id) {
        return restaurantService.removeRestaurant(id)
                .map(mapToRestaurantControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Function<RestaurantDto, RestaurantControllerResponseDto> mapToRestaurantControllerResponseDto() {
        return restaurantDto ->
                RestaurantControllerResponseDto.builder()
                        .id(restaurantDto.getId())
                        .name(restaurantDto.getName())
                        .address(restaurantDto.getAddress())
                        .phone(restaurantDto.getPhone())
                        .employeeCount(restaurantDto.getEmployeeCount())
                        .companyId(restaurantDto.getCompanyId())
                        .build();
    }

    @Override
    public Function<RestaurantControllerRequestDto, RestaurantDto> mapToRestaurantDto() {
        return restaurantControllerRequestDto ->
                RestaurantDto.builder()
                        .name(restaurantControllerRequestDto.getName())
                        .address(restaurantControllerRequestDto.getAddress())
                        .phone(restaurantControllerRequestDto.getPhone())
                        .employeeCount(restaurantControllerRequestDto.getEmployeeCount())
                        .companyId(restaurantControllerRequestDto.getCompanyId())
                        .build();
    }
}
