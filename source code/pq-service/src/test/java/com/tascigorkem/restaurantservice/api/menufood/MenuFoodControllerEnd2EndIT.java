package com.tascigorkem.restaurantservice.api.menufood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.food.FoodDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.menufood.MenuFoodDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodEntity;
import com.tascigorkem.restaurantservice.infrastructure.food.FoodRepository;
import com.tascigorkem.restaurantservice.infrastructure.menu.MenuEntity;
import com.tascigorkem.restaurantservice.infrastructure.menu.MenuRepository;
import com.tascigorkem.restaurantservice.infrastructure.menufood.MenuFoodEntity;
import com.tascigorkem.restaurantservice.infrastructure.menufood.MenuFoodRepository;
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
class MenuFoodControllerEnd2EndIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MenuFoodRepository menuFoodRepository;

    @Test
    void getCompany() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        fakeMenuDto.setRestaurantId(fakeRestaurantId);

        UUID fakeFoodId = DomainModelFaker.fakeId();
        FoodDto fakeFoodDto = DomainModelFaker.getFakeFoodDto(fakeFoodId);

        UUID fakeMenuFoodId = DomainModelFaker.fakeId();
        MenuFoodDto fakeMenuFoodDto = MenuFoodDto.builder()
                .id(fakeMenuFoodId)
                .menuId(fakeMenuId)
                .foodId(fakeFoodId)
                .foodName(fakeFoodDto.getName())
                .originalPrice(fakeFoodDto.getPrice())
                .extended(true)
                .extendedPrice(DomainModelFaker.fakePrice())
                .build();

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare company entity
        CompanyEntity companyEntity = CompanyEntity.builder()
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
                .build();

        // prepare restaurant entity
        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
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
                .build();

        // prepare menu entity
        MenuEntity menuEntity = MenuEntity.builder()
                .id(fakeMenuId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuDto.getName())
                .menuType(fakeMenuDto.getMenuType())
                .restaurantId(fakeRestaurantId)
                .build();

        // prepare food entity
        FoodEntity foodEntity = FoodEntity.builder()
                .id(fakeFoodId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeFoodDto.getName())
                .vegetable(fakeFoodDto.isVegetable())
                .price(fakeFoodDto.getPrice())
                .imageUrl(fakeFoodDto.getImageUrl())
                .build();

        // prepare menu-food relation entity
        MenuFoodEntity menuFoodEntity = MenuFoodEntity.builder()
                .id(fakeMenuFoodId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .extended(true)
                .extendedPrice(fakeMenuFoodDto.getExtendedPrice())
                .menuId(fakeMenuId)
                .foodId(fakeFoodId)
                .build();

        // prepare db, delete all entities and insert one company, then restaurant, then three menus
        menuFoodRepository.deleteAll()
                .then(menuRepository.deleteAll())
                .then(restaurantRepository.deleteAll())
                .then(companyRepository.deleteAll())
                // inserts
                .then(companyRepository.save(companyEntity))
                .then(restaurantRepository.save(restaurantEntity))
                .then(menuRepository.save(menuEntity))
                .then(foodRepository.save(foodEntity))
                .then(menuFoodRepository.save(menuFoodEntity))
                .block();

        // act
        client.get().uri("/menus/" + fakeMenuId + "/foods/" + fakeFoodId)
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
    }
}

