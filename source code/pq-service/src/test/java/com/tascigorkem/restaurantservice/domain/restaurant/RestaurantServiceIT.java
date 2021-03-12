package com.tascigorkem.restaurantservice.domain.restaurant;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantDto;
import com.tascigorkem.restaurantservice.domain.restaurant.RestaurantPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
import com.tascigorkem.restaurantservice.infrastructure.menu.MenuEntity;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantEntity;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantPersistenceAdapter;
import com.tascigorkem.restaurantservice.infrastructure.restaurant.RestaurantRepository;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
class RestaurantServiceIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantPersistencePort restaurantPersistencePort;

    @Test
    void getRestaurantById() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);

        UUID fakeRestaurantId = DomainModelFaker.fakeId();
        RestaurantDto fakeRestaurantDto = DomainModelFaker.getFakeRestaurantDto(fakeRestaurantId);
        fakeRestaurantDto.setCompanyId(fakeCompanyId);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one company, then three restaurants
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

        restaurantPersistencePort = new RestaurantPersistenceAdapter(restaurantRepository);

        // act
        Mono<RestaurantDto> result = restaurantPersistencePort.getRestaurantById(fakeRestaurantId);

        // assert

        // option 1
        StepVerifier.create(result)
                .expectNext(fakeRestaurantDto)
                .verifyComplete();

        // option 2
//        assertAll(
//                () -> assertEquals(fakeRestaurantDto.getId(), restaurantDto.getId()),
//                () -> assertEquals(fakeRestaurantDto.getName(), restaurantDto.getName()),
//        );
    }
}
