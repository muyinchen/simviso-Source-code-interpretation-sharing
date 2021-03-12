package com.tascigorkem.restaurantservice.domain.company;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyPersistencePort companyPersistencePort;

    public CompanyServiceImpl(CompanyPersistencePort companyPersistencePort) {
        this.companyPersistencePort = companyPersistencePort;
    }

    @Override
    public Flux<CompanyDto> getAllCompanies() {
        return companyPersistencePort.getAllCompanies();
    }

    @Override
    public Mono<CompanyDto> getCompanyById(UUID id) {
        return companyPersistencePort.getCompanyById(id);
    }

    @Override
    public Mono<CompanyDto> addCompany(CompanyDto companyDto) {
        return companyPersistencePort.addCompany(companyDto);
    }

    @Override
    public Mono<CompanyDto> updateCompany(CompanyDto companyDto) {
        return companyPersistencePort.updateCompany(companyDto);
    }

    @Override
    public Mono<CompanyDto> removeCompany(UUID id) {
        return companyPersistencePort.removeCompany(id);
    }

}
