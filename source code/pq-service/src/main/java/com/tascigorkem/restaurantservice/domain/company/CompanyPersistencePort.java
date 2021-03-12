package com.tascigorkem.restaurantservice.domain.company;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CompanyPersistencePort {

    Flux<CompanyDto> getAllCompanies();

    Mono<CompanyDto> getCompanyById(UUID id);

    Mono<CompanyDto> addCompany(CompanyDto companyDto);

    Mono<CompanyDto> updateCompany(CompanyDto companyDto);

    Mono<CompanyDto> removeCompany(UUID id);
}
