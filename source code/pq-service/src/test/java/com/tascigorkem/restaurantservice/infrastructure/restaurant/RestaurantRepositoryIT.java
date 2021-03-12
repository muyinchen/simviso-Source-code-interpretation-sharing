package com.tascigorkem.restaurantservice.infrastructure.restaurant;

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
class RestaurantRepositoryIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void testGetAllRestaurants() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId1 = DomainModelFaker.fakeId();
        UUID fakeRestaurantId2 = DomainModelFaker.fakeId();
        UUID fakeRestaurantId3 = DomainModelFaker.fakeId();

        RestaurantDto fakeRestaurantDto1 = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId1);
        RestaurantDto fakeRestaurantDto2 = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId2);
        RestaurantDto fakeRestaurantDto3 = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId3);

        fakeRestaurantDto1.setCompanyId(fakeCompanyId);
        fakeRestaurantDto2.setCompanyId(fakeCompanyId);
        fakeRestaurantDto3.setCompanyId(fakeCompanyId);

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

        // prepare restaurant entities
        RestaurantEntity restaurantEntity1 = RestaurantEntity.builder()
                .id(fakeRestaurantId1)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeRestaurantDto1.getName())
                .address(fakeRestaurantDto1.getAddress())
                .phone(fakeRestaurantDto1.getPhone())
                .employeeCount(fakeRestaurantDto1.getEmployeeCount())
                .companyId(fakeCompanyId)
                .build();
        RestaurantEntity restaurantEntity2 = RestaurantEntity.builder()
                .id(fakeRestaurantId2)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeRestaurantDto2.getName())
                .address(fakeRestaurantDto2.getAddress())
                .phone(fakeRestaurantDto2.getPhone())
                .employeeCount(fakeRestaurantDto2.getEmployeeCount())
                .companyId(fakeCompanyId)
                .build();
        RestaurantEntity restaurantEntity3 = RestaurantEntity.builder()
                .id(fakeRestaurantId3)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeRestaurantDto3.getName())
                .address(fakeRestaurantDto3.getAddress())
                .phone(fakeRestaurantDto3.getPhone())
                .employeeCount(fakeRestaurantDto3.getEmployeeCount())
                .companyId(fakeCompanyId)
                .build();

        // prepare db, delete all entities and insert one company, then three restaurants
        restaurantRepository.deleteAll()
                .then(companyRepository.deleteAll())
                .then(companyRepository.save(companyEntity))
                .then(restaurantRepository.save(restaurantEntity1))
                .then(restaurantRepository.save(restaurantEntity2))
                .then(restaurantRepository.save(restaurantEntity3))
                .block();

        // act
        Flux<RestaurantEntity> result = restaurantRepository.findAll();
        List<RestaurantEntity> resultRestaurantEntityList = result.collectList().block();

        // assert
        assertNotNull(resultRestaurantEntityList);
        assertEquals(resultRestaurantEntityList.size(), 3);

        RestaurantEntity resultRestaurantEntity1 = resultRestaurantEntityList.stream()
                .filter(companyEntityItem -> fakeRestaurantDto1.getId().equals(companyEntityItem.getId())).findFirst().get();

        RestaurantEntity resultRestaurantEntity2 = resultRestaurantEntityList.stream()
                .filter(companyEntityItem -> fakeRestaurantDto2.getId().equals(companyEntityItem.getId())).findFirst().get();

        RestaurantEntity resultRestaurantEntity3 = resultRestaurantEntityList.stream()
                .filter(companyEntityItem -> fakeRestaurantDto3.getId().equals(companyEntityItem.getId())).findFirst().get();

        assertAll(
                () -> assertEquals(fakeRestaurantDto1.getId(), resultRestaurantEntity1.getId()),
                () -> assertEquals(fakeRestaurantDto1.getName(), resultRestaurantEntity1.getName()),
                () -> assertEquals(fakeRestaurantDto1.getAddress(), resultRestaurantEntity1.getAddress()),
                () -> assertEquals(fakeRestaurantDto1.getPhone(), resultRestaurantEntity1.getPhone()),
                () -> assertEquals(fakeRestaurantDto1.getEmployeeCount(), resultRestaurantEntity1.getEmployeeCount()),
                () -> assertEquals(fakeRestaurantDto1.getCompanyId(), resultRestaurantEntity1.getCompanyId()),

                () -> assertEquals(fakeRestaurantDto2.getId(), resultRestaurantEntity2.getId()),
                () -> assertEquals(fakeRestaurantDto2.getName(), resultRestaurantEntity2.getName()),
                () -> assertEquals(fakeRestaurantDto2.getAddress(), resultRestaurantEntity2.getAddress()),
                () -> assertEquals(fakeRestaurantDto2.getPhone(), resultRestaurantEntity2.getPhone()),
                () -> assertEquals(fakeRestaurantDto2.getEmployeeCount(), resultRestaurantEntity2.getEmployeeCount()),
                () -> assertEquals(fakeRestaurantDto2.getCompanyId(), resultRestaurantEntity2.getCompanyId()),

                () -> assertEquals(fakeRestaurantDto3.getId(), resultRestaurantEntity3.getId()),
                () -> assertEquals(fakeRestaurantDto3.getName(), resultRestaurantEntity3.getName()),
                () -> assertEquals(fakeRestaurantDto3.getAddress(), resultRestaurantEntity3.getAddress()),
                () -> assertEquals(fakeRestaurantDto3.getPhone(), resultRestaurantEntity3.getPhone()),
                () -> assertEquals(fakeRestaurantDto3.getEmployeeCount(), resultRestaurantEntity3.getEmployeeCount()),
                () -> assertEquals(fakeRestaurantDto3.getCompanyId(), resultRestaurantEntity3.getCompanyId())
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
        Mono<RestaurantEntity> result = restaurantRepository.findById(fakeRestaurantId);
        RestaurantEntity restaurantEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(fakeRestaurantDto.getId(), restaurantEntity.getId()),
                () -> assertEquals(fakeRestaurantDto.getName(), restaurantEntity.getName()),
                () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantEntity.getAddress()),
                () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantEntity.getPhone()),
                () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantEntity.getEmployeeCount()),
                () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantEntity.getCompanyId())
        );
    }

    @Test
    void givenRestaurantId_whenCreateRestaurantTwiceWithSameId_thenFails() {
        // arrange
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
        // insert second entity
        Executable createRestaurant = () ->
                restaurantRepository.save(RestaurantEntity.builder()
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
                        .build()).block();

        // assert
        assertThrows(DataIntegrityViolationException.class, createRestaurant);

    }

    @Test
    void testRestaurantFields() {
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one entity
        RestaurantEntity insertedRestaurant = RestaurantEntity.builder()
                .id(fakeRestaurantId)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeRestaurantDto.getName())
                .name(fakeRestaurantDto.getName())
                .address(fakeRestaurantDto.getAddress())
                .phone(fakeRestaurantDto.getPhone())
                .employeeCount(fakeRestaurantDto.getEmployeeCount())
                .companyId(fakeCompanyId)
                .build();

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
        Mono<RestaurantEntity> result = restaurantRepository.findById(fakeRestaurantId);
        RestaurantEntity restaurantEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(fakeRestaurantDto.getId(), restaurantEntity.getId()),
                () -> assertEquals(fakeRestaurantDto.getName(), restaurantEntity.getName()),
                () -> assertEquals(fakeRestaurantDto.getAddress(), restaurantEntity.getAddress()),
                () -> assertEquals(fakeRestaurantDto.getPhone(), restaurantEntity.getPhone()),
                () -> assertEquals(fakeRestaurantDto.getEmployeeCount(), restaurantEntity.getEmployeeCount()),
                () -> assertEquals(fakeRestaurantDto.getCompanyId(), restaurantEntity.getCompanyId())
        );
    }

}
