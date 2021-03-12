package com.tascigorkem.restaurantservice.domain.company;

import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CompanyServiceTest {

    private final CompanyPersistencePort companyPersistencePort = mock(CompanyPersistencePort.class);
    private final CompanyService subject = new CompanyServiceImpl(companyPersistencePort);

    /**
     * Unit test for CompanyService:getAllCompanies
     */
    @Test
    void testGetAllCompanies() {
        // arrange
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(DomainModelFaker.fakeId());
        List<CompanyDto> companyDtoList = Arrays.asList(fakeCompanyDto, fakeCompanyDto, fakeCompanyDto);
        when(companyPersistencePort.getAllCompanies()).thenReturn(Flux.fromIterable(companyDtoList));

        // act
        Flux<CompanyDto> result = subject.getAllCompanies();

        //assert
        StepVerifier.create(result)
                .expectNext(companyDtoList.get(0))
                .expectNext(companyDtoList.get(1))
                .expectNext(companyDtoList.get(2))
                .expectComplete()
                .verify();

        verify(companyPersistencePort).getAllCompanies();
    }

    /**
     * Unit test for CompanyService:getCompanyById
     */
    @Test
    void givenCompanyId_whenGetCompany_andCompanyExists_thenReturnCompany() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyPersistencePort.getCompanyById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyDto));

        // act
        Mono<CompanyDto> result = subject.getCompanyById(fakeCompanyId);

        // assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyPersistencePort).getCompanyById(fakeCompanyId);

    }

    /**
     * Unit test for CompanyService:addCompany
     */
    @Test
    void givenCompanyControllerRequestDto_whenCreateCompany_thenReturnSuccessful_andReturnCompany() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyPersistencePort.addCompany(fakeCompanyDto)).thenReturn(Mono.just(fakeCompanyDto));

        // act
        Mono<CompanyDto> result = subject.addCompany(fakeCompanyDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyPersistencePort).addCompany(fakeCompanyDto);
    }

    /**
     * Unit test for CompanyService:updateCompany
     */
    @Test
    void givenCompanyId_andCompanyDto_whenUpdateCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyPersistencePort.updateCompany(fakeCompanyDto)).thenReturn(Mono.just(fakeCompanyDto));

        // act
        Mono<CompanyDto> result = subject.updateCompany(fakeCompanyDto);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyPersistencePort).updateCompany(fakeCompanyDto);
    }

    /**
     * Unit test for CompanyService:removeCompany
     */
    @Test
    void givenCompanyId_whenRemoveCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = DomainModelFaker.fakeId();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyPersistencePort.removeCompany(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyDto));

        // act
        Mono<CompanyDto> result = subject.removeCompany(fakeCompanyId);

        //assert
        StepVerifier.create(result)
                .expectNext(fakeCompanyDto)
                .verifyComplete();

        verify(companyPersistencePort).removeCompany(fakeCompanyId);
    }
}
