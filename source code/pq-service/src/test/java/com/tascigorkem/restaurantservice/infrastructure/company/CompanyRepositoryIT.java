package com.tascigorkem.restaurantservice.infrastructure.company;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
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
class CompanyRepositoryIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testGetAllCompanies() {
        // arrange
        UUID fakeCompanyId1 = DomainModelFaker.fakeId();
        UUID fakeCompanyId2 = DomainModelFaker.fakeId();
        UUID fakeCompanyId3 = DomainModelFaker.fakeId();

        CompanyDto fakeCompanyDto1 = DomainModelFaker.getFakeCompanyDto(fakeCompanyId1);
        CompanyDto fakeCompanyDto2 = DomainModelFaker.getFakeCompanyDto(fakeCompanyId2);
        CompanyDto fakeCompanyDto3 = DomainModelFaker.getFakeCompanyDto(fakeCompanyId3);

        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        CompanyEntity companyEntity1 = CompanyEntity.builder()
                .id(fakeCompanyId1)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeCompanyDto1.getName())
                .address(fakeCompanyDto1.getAddress())
                .phone(fakeCompanyDto1.getPhone())
                .emailAddress(fakeCompanyDto1.getEmailAddress())
                .websiteUrl(fakeCompanyDto1.getWebsiteUrl())
                .build();
        CompanyEntity companyEntity2 = CompanyEntity.builder()
                .id(fakeCompanyId2)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeCompanyDto2.getName())
                .address(fakeCompanyDto2.getAddress())
                .phone(fakeCompanyDto2.getPhone())
                .emailAddress(fakeCompanyDto2.getEmailAddress())
                .websiteUrl(fakeCompanyDto2.getWebsiteUrl())
                .build();
        CompanyEntity companyEntity3 = CompanyEntity.builder()
                .id(fakeCompanyId3)
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(fakeCompanyDto3.getName())
                .address(fakeCompanyDto3.getAddress())
                .phone(fakeCompanyDto3.getPhone())
                .emailAddress(fakeCompanyDto3.getEmailAddress())
                .websiteUrl(fakeCompanyDto3.getWebsiteUrl())
                .build();

        // prepare db, delete all entities and insert one entity
        companyRepository.deleteAll()
                .then(companyRepository.save(companyEntity1))
                .then(companyRepository.save(companyEntity2))
                .then(companyRepository.save(companyEntity3))
                .block();

        // act
        Flux<CompanyEntity> result = companyRepository.findAll();
        List<CompanyEntity> resultCompanyEntityList = result.collectList().block();

        // assert
        assertNotNull(resultCompanyEntityList);
        assertEquals(resultCompanyEntityList.size(), 3);

        CompanyEntity resultCompanyEntity1 = resultCompanyEntityList.stream()
                .filter(companyEntityItem -> fakeCompanyDto1.getId().equals(companyEntityItem.getId())).findFirst().get();

        CompanyEntity resultCompanyEntity2 = resultCompanyEntityList.stream()
                .filter(companyEntityItem -> fakeCompanyDto2.getId().equals(companyEntityItem.getId())).findFirst().get();

        CompanyEntity resultCompanyEntity3 = resultCompanyEntityList.stream()
                .filter(companyEntityItem -> fakeCompanyDto3.getId().equals(companyEntityItem.getId())).findFirst().get();

        assertAll(
                () -> assertEquals(fakeCompanyDto1.getId(), resultCompanyEntity1.getId()),
                () -> assertEquals(fakeCompanyDto1.getName(), resultCompanyEntity1.getName()),
                () -> assertEquals(fakeCompanyDto1.getAddress(), resultCompanyEntity1.getAddress()),
                () -> assertEquals(fakeCompanyDto1.getPhone(), resultCompanyEntity1.getPhone()),
                () -> assertEquals(fakeCompanyDto1.getEmailAddress(), resultCompanyEntity1.getEmailAddress()),
                () -> assertEquals(fakeCompanyDto1.getWebsiteUrl(), resultCompanyEntity1.getWebsiteUrl()),

                () -> assertEquals(fakeCompanyDto2.getId(), resultCompanyEntity2.getId()),
                () -> assertEquals(fakeCompanyDto2.getName(), resultCompanyEntity2.getName()),
                () -> assertEquals(fakeCompanyDto2.getAddress(), resultCompanyEntity2.getAddress()),
                () -> assertEquals(fakeCompanyDto2.getPhone(), resultCompanyEntity2.getPhone()),
                () -> assertEquals(fakeCompanyDto2.getEmailAddress(), resultCompanyEntity2.getEmailAddress()),
                () -> assertEquals(fakeCompanyDto2.getWebsiteUrl(), resultCompanyEntity2.getWebsiteUrl()),

                () -> assertEquals(fakeCompanyDto3.getId(), resultCompanyEntity3.getId()),
                () -> assertEquals(fakeCompanyDto3.getName(), resultCompanyEntity3.getName()),
                () -> assertEquals(fakeCompanyDto3.getAddress(), resultCompanyEntity3.getAddress()),
                () -> assertEquals(fakeCompanyDto3.getPhone(), resultCompanyEntity3.getPhone()),
                () -> assertEquals(fakeCompanyDto3.getEmailAddress(), resultCompanyEntity3.getEmailAddress()),
                () -> assertEquals(fakeCompanyDto3.getWebsiteUrl(), resultCompanyEntity3.getWebsiteUrl())
        );
    }

    @Test
    void findById() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, delete all entities and insert one entity
        companyRepository.deleteAll()
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
                .block();

        // act
        Mono<CompanyEntity> result = companyRepository.findById(fakeCompanyId);
        CompanyEntity companyEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(fakeCompanyDto.getId(), companyEntity.getId()),
                () -> assertEquals(fakeCompanyDto.getName(), companyEntity.getName()),
                () -> assertEquals(fakeCompanyDto.getAddress(), companyEntity.getAddress()),
                () -> assertEquals(fakeCompanyDto.getPhone(), companyEntity.getPhone()),
                () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyEntity.getEmailAddress()),
                () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyEntity.getWebsiteUrl())
        );
    }

    @Test
    void givenCompanyId_whenCreateCompanyTwiceWithSameId_thenFails() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, insert first entity
        companyRepository.deleteAll().then(
                companyRepository.save(CompanyEntity.builder()
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
                        .build())
        ).block();

        // act
        // insert second entity
        Executable createCompany = () ->
                companyRepository.save(CompanyEntity.builder()
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
                        .build()).block();

        // assert
        assertThrows(DataIntegrityViolationException.class, createCompany);

    }

    @Test
    void testCompanyFields() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        // prepare db, insert first entity
        CompanyEntity insertedCompany = CompanyEntity.builder()
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

        companyRepository.deleteAll()
                .then(companyRepository.save(insertedCompany))
                .block();

        // act
        Mono<CompanyEntity> result = companyRepository.findById(fakeCompanyId);
        CompanyEntity companyEntity = result.block();

        // assert
        assertAll(
                () -> assertEquals(insertedCompany.getId(), companyEntity.getId()),
                () -> assertEquals(insertedCompany.getCreationTime(), companyEntity.getCreationTime()),
                () -> assertEquals(insertedCompany.getUpdateTime(), companyEntity.getUpdateTime()),
                () -> assertEquals(insertedCompany.getStatus(), companyEntity.getStatus()),
                () -> assertEquals(insertedCompany.getDeletionTime(), companyEntity.getDeletionTime()),
                () -> assertEquals(insertedCompany.isDeleted(), companyEntity.isDeleted()),
                () -> assertEquals(insertedCompany.getName(), companyEntity.getName()),
                () -> assertEquals(insertedCompany.getAddress(), companyEntity.getAddress()),
                () -> assertEquals(insertedCompany.getPhone(), companyEntity.getPhone()),
                () -> assertEquals(insertedCompany.getEmailAddress(), companyEntity.getEmailAddress()),
                () -> assertEquals(insertedCompany.getWebsiteUrl(), companyEntity.getWebsiteUrl())
        );
    }

}
