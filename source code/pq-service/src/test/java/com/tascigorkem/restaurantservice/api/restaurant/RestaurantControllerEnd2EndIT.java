package com.tascigorkem.restaurantservice.api.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantEntity;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RestaurantControllerEnd2EndIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getRestaurant() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one entity
        restaurantRepository.deleteAll()
                .then(companyRepository.deleteAll())
                .then(companyRepository.save(CompanyEntity.builder()
                        .id(fakeCompanyId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeCompanyDto.getName())
                        .address(fakeCompanyDto.getAddress())
                        .phone(fakeCompanyDto.getPhone())
                        .emailAddress(fakeCompanyDto.getEmailAddress())
                        .websiteUrl(fakeCompanyDto.getWebsiteUrl())
                        .build()))
                .then(restaurantRepository.save(RestaurantEntity.builder()
                        .id(fakeRestaurantId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeRestaurantDto.getName())
                        .address(fakeRestaurantDto.getAddress())
                        .phone(fakeRestaurantDto.getPhone())
                        .employeeCount(fakeRestaurantDto.getEmployeeCount())
                        .companyId(fakeCompanyId)
                        .build()))
                .block();

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
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(fakeRestaurantId, restaurantResponseDto.getId()),
                            () -> assertEquals(fakeRestaurantDto.getName(), restaurantResponseDto.getName()),
                            () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantResponseDto.getAddress()),
                            () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantResponseDto.getPhone()),
                            () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantResponseDto.getEmployeeCount()),
                            () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantResponseDto.getCompanyId())
                    );
                });
    }
}

