package com.tascigorkem.restaurantservice.api.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.ApiModelFaker;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantService;
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

@WebFluxTest(RestaurantController.class)
class RestaurantControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private RestaurantService restaurantService;

    private RestaurantController subject = new RestaurantControllerImpl(restaurantService);

    /**
     * Unit test for RestaurantController:getAllRestaurants
     */
    @Test
    void testGetAllRestaurants() {
        // arrange
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(DomainModelFaker.fakeId());
        List<RestaurantDto> restaurantDtoList = Arrays.asList(fakeRestaurantDto, fakeRestaurantDto, fakeRestaurantDto);
        when(restaurantService.getAllRestaurants()).thenReturn(Flux.fromIterable(restaurantDtoList));

        // act
        client.get().uri("/restaurants")
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

                    List<RestaurantControllerResponseDto> responseDtoList = Arrays.asList(
                            objectMapper.convertValue(response.getPayload(), RestaurantControllerResponseDto[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(responseDto ->
                            assertAll(
                                    () -> assertEquals(fakeRestaurantDto.getId(), responseDto.getId()),
                                    () -> assertEquals(fakeRestaurantDto.getName(), responseDto.getName()),
                                    () -> assertEquals(fakeRestaurantDto.getAddress(), responseDto.getAddress()),
                                    () -> assertEquals(fakeRestaurantDto.getPhone(), responseDto.getPhone()),
                                    () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), responseDto.getEmployeeCount()),
                                    () -> assertEquals(fakeRestaurantDto.getCompanyId(), responseDto.getCompanyId())
                            ));

                });
        verify(restaurantService).getAllRestaurants();
    }

    /**
     * Unit test for RestaurantController:getRestaurantById
     */
    @Test
    void givenRestaurantId_whenGetRestaurant_andRestaurantExists_thenReturnRestaurant() {
        // arrange
        UUID fakeRestaurantId = UUID.randomUUID();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantService.getRestaurantById(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantDto));
        // act
        client.get().uri("/restaurants/" + fakeRestaurantId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    RestaurantControllerResponseDto restaurantResponseDto = objectMapper
                            .convertValue(response.getPayload(), RestaurantControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(fakeRestaurantId, restaurantResponseDto.getId()),
                            () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                            () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                            () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                            () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                            () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                    );
                });
        verify(restaurantService).getRestaurantById(fakeRestaurantId);
    }

    /**
     * Unit test for RestaurantController:addRestaurant
     */
    @Test
    void givenRestaurantControllerRequestDto_whenCreateRestaurant_thenReturnSuccessful_andReturnRestaurant() {
        // arrange
        RestaurantControllerRequestDto fakeRestaurantControllerRequestDto = ApiModelFaker.getRestaurantControllerRequestDto();
        RestaurantDto fakeRestaurantDto = subject.mapToRestaurantDto().apply(fakeRestaurantControllerRequestDto);
        when(restaurantService.addRestaurant(fakeRestaurantDto)).thenReturn(Mono.just(fakeRestaurantDto));
        // act
        client.post().uri("/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeRestaurantControllerRequestDto), RestaurantControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                            RestaurantControllerResponseDto restaurantResponseDto = objectMapper
                                    .convertValue(response.getPayload(), RestaurantControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                                    () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                                    () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                                    () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                                    () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                            );
                        }

                );
        verify(restaurantService).addRestaurant(fakeRestaurantDto);
    }

    /**
     * Unit test for RestaurantController:updateRestaurant
     */
    @Test
    void givenRestaurantRestaurantControllerRequestDto_andRestaurantControllerRequestDto_whenUpdateRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = UUID.randomUUID();
        RestaurantControllerRequestDto fakeRestaurantControllerRequestDto = ApiModelFaker.getRestaurantControllerRequestDto();
        RestaurantDto fakeRestaurantDto = subject.mapToRestaurantDto().apply(fakeRestaurantControllerRequestDto);
        fakeRestaurantDto.setId(fakeRestaurantId);
        when(restaurantService.updateRestaurant(fakeRestaurantDto)).thenReturn(Mono.just(fakeRestaurantDto));
        // act
        client.put().uri("/restaurants/" + fakeRestaurantId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeRestaurantControllerRequestDto), RestaurantControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    RestaurantControllerResponseDto restaurantResponseDto = objectMapper
                            .convertValue(response.getPayload(), RestaurantControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                                    () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                                    () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                                    () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                                    () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                            );
                        }

                );
        verify(restaurantService).updateRestaurant(fakeRestaurantDto);
    }

    /**
     * Unit test for RestaurantController:removeRestaurant
     */
    @Test
    void givenRestaurantId_whenRemoveRestaurant_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeRestaurantId = UUID.randomUUID();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        when(restaurantService.removeRestaurant(fakeRestaurantId)).thenReturn(Mono.just(fakeRestaurantDto));
        // act
        client.delete().uri("/restaurants/" + fakeRestaurantId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    RestaurantControllerResponseDto restaurantResponseDto = objectMapper
                            .convertValue(response.getPayload(), RestaurantControllerResponseDto.class);

                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                                    () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                                    () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                                    () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                                    () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                            );
                        }

                );
        verify(restaurantService).removeRestaurant(fakeRestaurantId);
    }
}
