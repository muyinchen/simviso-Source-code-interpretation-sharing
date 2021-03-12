package com.tascigorkem.restaurantservice.infrastructure.company;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyEntity;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyPersistenceAdapter;
import com.tascigorkem.restaurantservice.infrastructure.company.CompanyRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CompanyPersistenceAdapterTest {

    private final CompanyRepository companyRepository = mock(CompanyRepository.class);
    private final CompanyPersistenceAdapter subject = new CompanyPersistenceAdapter(companyRepository);

    /**
     * Unit test for CompanyPersistenceAdapter:getAllCompanies
     */
    @Test
    void testGetAllCompanies() {
        // arrange
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(DomainModelFaker.fakeId());
        List<CompanyDto> companyDtoList = Arrays.asList(fakeCompanyDto, fakeCompanyDto, fakeCompanyDto);
        List<CompanyEntity> companyEntityList = companyDtoList.stream().map(subject::mapToCompanyEntity).collect(Collectors.toList());
        when(companyRepository.findAll()).thenReturn(Flux.fromIterable(companyEntityList));

        // act
        Flux<CompanyDto> result = subject.getAllCompanies();

        //assert
        StepVerifier.create(result)
                .expectNext(companyDtoList.get(0))
                .expectNext(companyDtoList.get(1))
                .expectNext(companyDtoList.get(2))
                .expectComplete()
                .verify();

        verify(companyRepository).findAll();
    }

    /**
     * Unit test for CompanyPersistenceAdapter:getCompanyById
     */
    @Test
    void getCompanyById() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        CompanyEntity fakeCompanyEntity = subject.mapToCompanyEntity(fakeCompanyDto);
        when(companyRepository.findById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyEntity));

        // act
        Mono<CompanyDto> result = subject.getCompanyById(fakeCompanyId);

        // assert
        StepVerifier.create(result)
                .assertNext(companyEntity ->
                        assertThat(companyEntity)
                                .usingRecursiveComparison()
                                .isEqualTo(fakeCompanyDto))
                .verifyComplete();
    }

    /**
     * Unit test for CompanyPersistenceAdapter:addCompany
     */
    @Test
    void givenCompanyControllerRequestDto_whenCreateCompany_thenReturnSuccessful_andReturnCompany() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        CompanyEntity fakeCompanyEntity = subject.mapToCompanyEntity(fakeCompanyDto);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(Mono.just(fakeCompanyEntity));

        // act
        Mono<CompanyDto> result = subject.addCompany(fakeCompanyDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyRepository).save(any(CompanyEntity.class));
    }

    /**
     * Unit test for CompanyPersistenceAdapter:updateCompany
     */
    @Test
    void givenCompanyId_andCompanyDto_whenUpdateCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        CompanyEntity fakeCompanyEntity = subject.mapToCompanyEntity(fakeCompanyDto);
        when(companyRepository.findById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyEntity));
        when(companyRepository.save(fakeCompanyEntity)).thenReturn(Mono.just(fakeCompanyEntity));

        // act

        Mono<CompanyDto> result = subject.updateCompany(fakeCompanyDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyRepository).findById(fakeCompanyId);
        verify(companyRepository).save(any(CompanyEntity.class));
    }

    /**
     * Unit test for CompanyPersistenceAdapter:removeCompany
     */
    @Test
    void givenCompanyId_whenRemoveCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        CompanyEntity fakeCompanyEntity = subject.mapToCompanyEntity(fakeCompanyDto);
        when(companyRepository.findById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyEntity));
        when(companyRepository.save(fakeCompanyEntity)).thenReturn(Mono.just(fakeCompanyEntity));

        // act
        Mono<CompanyDto> result = subject.removeCompany(fakeCompanyId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyRepository).findById(fakeCompanyId);
        verify(companyRepository).save(any(CompanyEntity.class));
    }
}
