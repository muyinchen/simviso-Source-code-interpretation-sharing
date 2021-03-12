package com.tascigorkem.restaurantservice.domain.company;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.company.CompanyPersistencePort;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyPersistenceAdapter;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
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
class CompanyServiceIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyPersistencePort companyPersistencePort;

    @Test
    void getCompanyById() {
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

        companyPersistencePort = new CompanyPersistenceAdapter(companyRepository);

        // act
        Mono<CompanyDto> result = companyPersistencePort.getCompanyById(fakeCompanyId);

        // assert

        // option 1
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        // option 2
//        assertAll(
//                () -> assertEquals(fakeCompanyDto.getId(), companyDto.getId()),
//                () -> assertEquals(fakeCompanyDto.getName(), companyDto.getName())
//        );
    }
}
