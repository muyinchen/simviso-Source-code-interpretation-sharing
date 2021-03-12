package com.tascigorkem.restaurantservice.api.menufood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.ApiModelFaker;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(MenuFoodController.class)
public class MenuFoodControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private MenuFoodService menuFoodService;

    private MenuFoodController subject = new MenuFoodControllerImpl(menuFoodService);

    /**
     * Unit test for MenuFoodController:getAllMenuFoods
     */
    @Test
    void testGetAllMenuFoods() {
        // arrange
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(DomainModelFaker.fakeId());
        List<MenuFoodDto> menuFoodDtoList = Arrays.asList(fakeMenuFoodDto, fakeMenuFoodDto, fakeMenuFoodDto);
        when(menuFoodService.getAllMenuFoods()).thenReturn(Flux.fromIterable(menuFoodDtoList));

        // act
        client.get().uri("/menus/foods")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    List<MenuFoodControllerResponseDto> responseDtoList = Arrays.asList(
                            objectMapper.convertValue(response.getPayload(), MenuFoodControllerResponseDto[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(menuFoodResponseDto ->
                            assertAll(
                                    () -> assertEquals(fakeMenuFoodDto.getId(), menuFoodResponseDto.getId()),
                                    () -> assertEquals(fakeMenuFoodDto.getMenuId(), menuFoodResponseDto.getMenuId()),
                                    () -> assertEquals(fakeMenuFoodDto.getFoodId(), menuFoodResponseDto.getFoodId()),
                                    () -> assertEquals(fakeMenuFoodDto.getFoodName(), menuFoodResponseDto.getFoodName()),
                                    () -> assertEquals(fakeMenuFoodDto.getOriginalPrice(), menuFoodResponseDto.getOriginalPrice()),
                                    () -> assertEquals(fakeMenuFoodDto.isExtended(), menuFoodResponseDto.isExtended()),
                                    () -> assertEquals(fakeMenuFoodDto.getExtendedPrice(), menuFoodResponseDto.getExtendedPrice())
                            ));

                });
        verify(menuFoodService).getAllMenuFoods();
    }

    /**
     * Unit test for MenuFoodController:getFoodPriceInfoByMenuId
     */
    @Test
    void getFoodPriceInfoByMenuId() {
        // arrange
        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = DomainModelFaker.getFakeMenuFoodDto(fakeMenuFoodId);
        when(menuFoodService.getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId()))
                .thenReturn(Mono.just(fakeMenuFoodDto));
        // act
        client.get().uri("/menus/" + fakeMenuFoodDto.getMenuId() + "/foods/" + fakeMenuFoodDto.getFoodId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    MenuFoodControllerResponseDto menuFoodResponseDto = objectMapper
                            .convertValue(response.getPayload(), MenuFoodControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(fakeMenuFoodId, menuFoodResponseDto.getId()),
                            () -> assertEquals(fakeMenuFoodDto.getMenuId(), menuFoodResponseDto.getMenuId()),
                            () -> assertEquals(fakeMenuFoodDto.getFoodId(), menuFoodResponseDto.getFoodId()),
                            () -> assertEquals(fakeMenuFoodDto.getFoodName(), menuFoodResponseDto.getFoodName()),
                            () -> assertEquals(fakeMenuFoodDto.getOriginalPrice(), menuFoodResponseDto.getOriginalPrice()),
                            () -> assertEquals(fakeMenuFoodDto.isExtended(), menuFoodResponseDto.isExtended()),
                            () -> assertEquals(fakeMenuFoodDto.getExtendedPrice(), menuFoodResponseDto.getExtendedPrice())
                    );
                });
        verify(menuFoodService).getFoodPriceInfoByMenuId(fakeMenuFoodDto.getMenuId(), fakeMenuFoodDto.getFoodId());
    }

    /**
     * Unit test for MenuFoodController:addMenuFood
     */
    @Test
    void givenMenuFoodControllerRequestDto_whenCreateMenuFood_thenReturnSuccessful_andReturnMenuFood() {
        // arrange
        UUID menuId = DomainModelFaker.fakeId();
        UUID foodId = DomainModelFaker.fakeId();
        MenuFoodControllerRequestDto fakeMenuFoodControllerRequestDto = ApiModelFaker.getMenuFoodControllerRequestDto();
        MenuFoodDto fakeMenuFoodDto = subject.mapToMenuFoodDto().apply(fakeMenuFoodControllerRequestDto);
        fakeMenuFoodDto.setMenuId(menuId);
        fakeMenuFoodDto.setFoodId(foodId);
        when(menuFoodService.addMenuFood(fakeMenuFoodDto)).thenReturn(Mono.just(fakeMenuFoodDto));
        // act
        client.post().uri("/menus/" + menuId + "/foods/" + foodId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeMenuFoodControllerRequestDto), MenuFoodControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                            MenuFoodControllerResponseDto menuFoodResponseDto = objectMapper
                                    .convertValue(response.getPayload(), MenuFoodControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(menuId, menuFoodResponseDto.getMenuId()),
                                    () -> assertEquals(foodId, menuFoodResponseDto.getFoodId()),
                                    () -> assertEquals(fakeMenuFoodDto.getFoodName(), menuFoodResponseDto.getFoodName()),
                                    () -> assertEquals(fakeMenuFoodDto.getOriginalPrice(), menuFoodResponseDto.getOriginalPrice()),
                                    () -> assertEquals(fakeMenuFoodDto.isExtended(), menuFoodResponseDto.isExtended()),
                                    () -> assertEquals(fakeMenuFoodDto.getExtendedPrice(), menuFoodResponseDto.getExtendedPrice())
                            );
                        }

                );
        verify(menuFoodService).addMenuFood(fakeMenuFoodDto);
    }

}
