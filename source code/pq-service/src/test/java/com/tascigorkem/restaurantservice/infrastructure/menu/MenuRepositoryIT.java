package com.tascigorkem.restaurantservice.infrastructure.menu;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.menu.MenuDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantEntity;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuRepositoryIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void testGetAllMenus() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        UUID fakeMenuId1 = DomainModelFaker.fakeId();
        UUID fakeMenuId2 = DomainModelFaker.fakeId();
        UUID fakeMenuId3 = DomainModelFaker.fakeId();

        MenuDto fakeMenuDto1 = DomainModelFaker.getFakeMenuDto(fakeMenuId1);
        MenuDto fakeMenuDto2 = DomainModelFaker.getFakeMenuDto(fakeMenuId2);
        MenuDto fakeMenuDto3 = DomainModelFaker.getFakeMenuDto(fakeMenuId3);

        fakeMenuDto1.setRestaurantId(fakeRestaurantId);
        fakeMenuDto2.setRestaurantId(fakeRestaurantId);
        fakeMenuDto3.setRestaurantId(fakeRestaurantId);

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

        // prepare menu entities
        MenuEntity menuEntity1 = MenuEntity.builder()
                .id(fakeMenuId1)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuDto1.getName())
                .menuType(fakeMenuDto1.getMenuType())
                .restaurantId(fakeMenuDto1.getRestaurantId())
                .build();
        MenuEntity menuEntity2 = MenuEntity.builder()
                .id(fakeMenuId2)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuDto2.getName())
                .menuType(fakeMenuDto2.getMenuType())
                .restaurantId(fakeMenuDto2.getRestaurantId())
                .build();
        MenuEntity menuEntity3 = MenuEntity.builder()
                .id(fakeMenuId3)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuDto3.getName())
                .menuType(fakeMenuDto3.getMenuType())
                .restaurantId(fakeMenuDto3.getRestaurantId())
                .build();

        // prepare db, delete all entities and insert one company, then restaurant, then three menus
        menuRepository.deleteAll()
                .then(restaurantRepository.deleteAll())
                .then(companyRepository.deleteAll())
                // inserts
                .then(companyRepository.save(companyEntity))
                .then(restaurantRepository.save(restaurantEntity))
                .then(menuRepository.save(menuEntity1))
                .then(menuRepository.save(menuEntity2))
                .then(menuRepository.save(menuEntity3))
                .block();

        // act
        Flux<MenuEntity> result = menuRepository.findAll();
        List<MenuEntity> resultMenuEntityList = result.collectList().block();

        // assert
        assertNotNull(resultMenuEntityList);
        assertEquals(resultMenuEntityList.size(), 3);

        MenuEntity resultMenuEntity1 = resultMenuEntityList.stream()
                .filter(companyEntityItem -> fakeMenuDto1.getId().equals(companyEntityItem.getId())).findFirst().get();

        MenuEntity resultMenuEntity2 = resultMenuEntityList.stream()
                .filter(companyEntityItem -> fakeMenuDto2.getId().equals(companyEntityItem.getId())).findFirst().get();

        MenuEntity resultMenuEntity3 = resultMenuEntityList.stream()
                .filter(companyEntityItem -> fakeMenuDto3.getId().equals(companyEntityItem.getId())).findFirst().get();

        assertAll(
                () -> assertEquals(fakeMenuDto1.getId(), resultMenuEntity1.getId()),
                () -> assertEquals(fakeMenuDto1.getName(), resultMenuEntity1.getName()),
                () -> assertEquals(fakeMenuDto1.getMenuType(), resultMenuEntity1.getMenuType()),
                () -> assertEquals(fakeMenuDto1.getRestaurantId(), resultMenuEntity1.getRestaurantId()),

                () -> assertEquals(fakeMenuDto2.getId(), resultMenuEntity2.getId()),
                () -> assertEquals(fakeMenuDto2.getName(), resultMenuEntity2.getName()),
                () -> assertEquals(fakeMenuDto2.getMenuType(), resultMenuEntity2.getMenuType()),
                () -> assertEquals(fakeMenuDto2.getRestaurantId(), resultMenuEntity2.getRestaurantId()),

                () -> assertEquals(fakeMenuDto3.getId(), resultMenuEntity3.getId()),
                () -> assertEquals(fakeMenuDto3.getName(), resultMenuEntity3.getName()),
                () -> assertEquals(fakeMenuDto3.getMenuType(), resultMenuEntity3.getMenuType()),
                () -> assertEquals(fakeMenuDto3.getRestaurantId(), resultMenuEntity3.getRestaurantId())
        );

    }

    @Test
    void findById() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        fakeMenuDto.setRestaurantId(fakeRestaurantId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one company, then one restaurant, then three menu
        menuRepository.deleteAll()
                .then(restaurantRepository.deleteAll())
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
                .then(menuRepository.save(MenuEntity.builder()
                        .id(fakeMenuId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeMenuDto.getName())
                        .menuType(fakeMenuDto.getMenuType())
                        .restaurantId(fakeMenuDto.getRestaurantId())
                        .build()))
                .block();

        // act
        Mono<MenuEntity> result = menuRepository.findById(fakeMenuId);
        MenuEntity menuEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(fakeMenuDto.getId(), menuEntity.getId()),
                () -> assertEquals(fakeMenuDto.getName(), menuEntity.getName()),
                () -> assertEquals(fakeMenuDto.getMenuType(), menuEntity.getMenuType()),
                () -> assertEquals(fakeMenuDto.getRestaurantId(), menuEntity.getRestaurantId())
        );
    }

    @Test
    void givenMenuId_whenCreateMenuTwiceWithSameId_thenFails() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        fakeMenuDto.setRestaurantId(fakeRestaurantId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one company, then one restaurant, then three menu
        menuRepository.deleteAll()
                .then(restaurantRepository.deleteAll())
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
                .then(menuRepository.save(MenuEntity.builder()
                        .id(fakeMenuId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeMenuDto.getName())
                        .menuType(fakeMenuDto.getMenuType())
                        .restaurantId(fakeMenuDto.getRestaurantId())
                        .build()))
                .block();

        // act
        // insert second entity
        Executable createMenu = () ->
                menuRepository.save(MenuEntity.builder()
                        .id(fakeMenuId)
                        .creationTime(now)
                        .updateTime(now)
                        .status(Status.CREATED)
                        .deleted(false)
                        .name(fakeMenuDto.getName())
                        .menuType(fakeMenuDto.getMenuType())
                        .restaurantId(fakeMenuDto.getRestaurantId())
                        .build()).block();

        // assert
        assertThrows(DataIntegrityViolationException.class, createMenu);

    }

    @Test
    void testMenuFields() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        UUID fakeMenuId = DomainModelFaker.fakeId();
        MenuDto fakeMenuDto = DomainModelFaker.getFakeMenuDto(fakeMenuId);
        fakeMenuDto.setRestaurantId(fakeRestaurantId);

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
        MenuEntity insertedMenu = MenuEntity.builder()
                .id(fakeMenuId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeMenuDto.getName())
                .menuType(fakeMenuDto.getMenuType())
                .restaurantId(fakeMenuDto.getRestaurantId())
                .build();

        // prepare db, delete all entities and insert one company, then restaurant, then three menus
        menuRepository.deleteAll()
                .then(restaurantRepository.deleteAll())
                .then(companyRepository.deleteAll())
                // inserts
                .then(companyRepository.save(companyEntity))
                .then(restaurantRepository.save(restaurantEntity))
                .then(menuRepository.save(insertedMenu))
                .block();

        // act
        Mono<MenuEntity> result = menuRepository.findById(fakeMenuId);
        MenuEntity menuEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(insertedMenu.getId(), menuEntity.getId()),
                () -> assertEquals(insertedMenu.getCreationTime(), menuEntity.getCreationTime()),
                () -> assertEquals(insertedMenu.getUpdateTime(), menuEntity.getUpdateTime()),
                () -> assertEquals(insertedMenu.getStatus(), menuEntity.getStatus()),
                () -> assertEquals(insertedMenu.getDeletionTime(), menuEntity.getDeletionTime()),
                () -> assertEquals(insertedMenu.isDeleted(), menuEntity.isDeleted()),
                () -> assertEquals(insertedMenu.getName(), menuEntity.getName()),
                () -> assertEquals(insertedMenu.getMenuType(), menuEntity.getMenuType()),
                () -> assertEquals(insertedMenu.getRestaurantId(), menuEntity.getRestaurantId())
        );
    }

}
