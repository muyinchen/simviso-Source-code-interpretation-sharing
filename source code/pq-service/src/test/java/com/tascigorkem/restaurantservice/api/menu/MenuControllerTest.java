package com.tascigorkem.restaurantservice.api.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.ApiModelFaker;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuService;
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

@WebFluxTest(MenuController.class)
class MenuControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private MenuService menuService;

    private MenuController subject = new MenuControllerImpl(menuService);

    /**
     * Unit test for MenuController:getAllMenus
     */
    @Test
    void testGetAllMenus() {
        // arrange
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(DomainModelFaker.fakeId());
        List<MenuDto> menuDtoList = Arrays.asList(fakeMenuDto, fakeMenuDto, fakeMenuDto);
        when(menuService.getAllMenus()).thenReturn(Flux.fromIterable(menuDtoList));

        // act
        client.get().uri("/menus")
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

                    List<MenuControllerResponseDto> responseDtoList = Arrays.asList(
                            objectMapper.convertValue(response.getPayload(), MenuControllerResponseDto[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(responseDto ->
                            assertAll(
                                    () -> assertEquals(fakeMenuDto.getId(), responseDto.getId()),
                                    () -> assertEquals(fakeMenuDto.getName(), responseDto.getName()),
                                    () -> assertEquals(fakeMenuDto.getMenuType(), responseDto.getMenuType()),
                                    () -> assertEquals(fakeMenuDto.getRestaurantId(), responseDto.getRestaurantId())
                            ));

                });
        verify(menuService).getAllMenus();
    }

    /**
     * Unit test for MenuController:getMenuById
     */
    @Test
    void givenMenuId_whenGetMenu_andMenuExists_thenReturnMenu() {
        // arrange
        UUID fakeMenuId = UUID.randomUUID();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuService.getMenuById(fakeMenuId)).thenReturn(Mono.just(fakeMenuDto));
        // act
        client.get().uri("/menus/" + fakeMenuId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    MenuControllerResponseDto menuResponseDto = objectMapper
                            .convertValue(response.getPayload(), MenuControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(fakeMenuId, menuResponseDto.getId()),
                            () -> assertEquals(fakeMenuDto.getName(), menuResponseDto.getName()),
                            () -> assertEquals(fakeMenuDto.getMenuType(), menuResponseDto.getMenuType()),
                            () -> assertEquals(fakeMenuDto.getRestaurantId(), menuResponseDto.getRestaurantId())
                    );
                });
        verify(menuService).getMenuById(fakeMenuId);
    }

    /**
     * Unit test for MenuController:addMenu
     */
    @Test
    void givenMenuControllerRequestDto_whenCreateMenu_thenReturnSuccessful_andReturnMenu() {
        // arrange
        MenuControllerRequestDto fakeMenuControllerRequestDto = ApiModelFaker.getMenuControllerRequestDto();
        MenuDto fakeMenuDto = subject.mapToMenuDto().apply(fakeMenuControllerRequestDto);
        when(menuService.addMenu(fakeMenuDto)).thenReturn(Mono.just(fakeMenuDto));
        // act
        client.post().uri("/menus")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeMenuControllerRequestDto), MenuControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                            MenuControllerResponseDto menuResponseDto = objectMapper
                                    .convertValue(response.getPayload(), MenuControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeMenuDto.getName(), menuResponseDto.getName()),
                                    () -> assertEquals(fakeMenuDto.getMenuType(), menuResponseDto.getMenuType()),
                                    () -> assertEquals(fakeMenuDto.getRestaurantId(), menuResponseDto.getRestaurantId())
                            );
                        }

                );
        verify(menuService).addMenu(fakeMenuDto);
    }

    /**
     * Unit test for MenuController:updateMenu
     */
    @Test
    void givenMenuMenuControllerRequestDto_andMenuControllerRequestDto_whenUpdateMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = UUID.randomUUID();
        MenuControllerRequestDto fakeMenuControllerRequestDto = ApiModelFaker.getMenuControllerRequestDto();
        MenuDto fakeMenuDto = subject.mapToMenuDto().apply(fakeMenuControllerRequestDto);
        fakeMenuDto.setId(fakeMenuId);
        when(menuService.updateMenu(fakeMenuDto)).thenReturn(Mono.just(fakeMenuDto));
        // act
        client.put().uri("/menus/" + fakeMenuId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeMenuControllerRequestDto), MenuControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    MenuControllerResponseDto menuResponseDto = objectMapper
                            .convertValue(response.getPayload(), MenuControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeMenuDto.getName(), menuResponseDto.getName()),
                                    () -> assertEquals(fakeMenuDto.getMenuType(), menuResponseDto.getMenuType()),
                                    () -> assertEquals(fakeMenuDto.getRestaurantId(), menuResponseDto.getRestaurantId())
                            );
                        }

                );
        verify(menuService).updateMenu(fakeMenuDto);
    }

    /**
     * Unit test for MenuController:removeMenu
     */
    @Test
    void givenMenuId_whenRemoveMenu_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeMenuId = UUID.randomUUID();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuService.removeMenu(fakeMenuId)).thenReturn(Mono.just(fakeMenuDto));
        // act
        client.delete().uri("/menus/" + fakeMenuId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    MenuControllerResponseDto menuResponseDto = objectMapper
                            .convertValue(response.getPayload(), MenuControllerResponseDto.class);

                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeMenuDto.getName(), menuResponseDto.getName()),
                                    () -> assertEquals(fakeMenuDto.getMenuType(), menuResponseDto.getMenuType()),
                                    () -> assertEquals(fakeMenuDto.getRestaurantId(), menuResponseDto.getRestaurantId())
                            );
                        }

                );
        verify(menuService).removeMenu(fakeMenuId);
    }
}
