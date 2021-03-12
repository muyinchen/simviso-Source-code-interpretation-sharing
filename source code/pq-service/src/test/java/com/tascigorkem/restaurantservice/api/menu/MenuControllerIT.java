package com.tascigorkem.restaurantservice.api.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MenuControllerIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @MockBean
    private MenuPersistencePort menuPersistencePort;

    @Test
    void getMenu() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeMenuId = UUID.randomUUID();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        when(menuPersistencePort.getMenuById(fakeMenuId)).thenReturn(Mono.just(fakeMenuDto));

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
                            () -> assertEquals(fakeMenuId, menuResponseDto.getId()),
                            () -> assertEquals(fakeMenuDto.getName(), menuResponseDto.getName()),
                            () -> assertEquals(fakeMenuDto.getMenuType(), menuResponseDto.getMenuType()),
                            () -> assertEquals(fakeMenuDto.getRestaurantId(), menuResponseDto.getRestaurantId())
                    );
                });

    }
}
