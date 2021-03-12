package com.tascigorkem.restaurantservice.infrastructure.company;

import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.company.CompanyPersistencePort;
import com.tascigorkem.restaurantservice.domain.exception.CompanyNotFoundException;
import com.tascigorkem.restaurantservice.infrastructure.base.Status;
import com.tascigorkem.restaurantservice.util.DateUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class CompanyPersistenceAdapter implements CompanyPersistencePort {

    private final CompanyRepository companyRepository;

    public CompanyPersistenceAdapter(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Flux<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().filter(companyEntity -> !companyEntity.isDeleted())
                .map(this::mapToCompanyDto);
    }

    @Override
    public Mono<CompanyDto> getCompanyById(UUID id) {
        return companyRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new CompanyNotFoundException("id", id.toString())))
                .map(this::mapToCompanyDto);
    }

    @Override
    public Mono<CompanyDto> addCompany(CompanyDto companyDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());
        return companyRepository.save(CompanyEntity.builder()
                .id(UUID.randomUUID())
                .creationTime(now)
                .updateTime(now)
                .status(Status.CREATED)
                .deleted(false)
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .phone(companyDto.getPhone())
                .emailAddress(companyDto.getEmailAddress())
                .websiteUrl(companyDto.getWebsiteUrl())
                .build())
                .map(this::mapToCompanyDto);
    }

    @Override
    public Mono<CompanyDto> updateCompany(CompanyDto companyDto) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return companyRepository.findById(companyDto.getId()).flatMap(companyEntity -> {
            companyEntity.setUpdateTime(now);
            companyEntity.setStatus(Status.UPDATED);
            companyEntity.setName(companyDto.getName());
            companyEntity.setAddress(companyDto.getAddress());
            companyEntity.setPhone(companyDto.getPhone());
            companyEntity.setEmailAddress(companyDto.getEmailAddress());
            companyEntity.setWebsiteUrl(companyDto.getWebsiteUrl());
            return companyRepository.save(companyEntity);
        })
                .switchIfEmpty(
                        Mono.error(new CompanyNotFoundException("id", companyDto.getId().toString())))
                .map(this::mapToCompanyDto);
    }

    @Override
    public Mono<CompanyDto> removeCompany(UUID id) {
        LocalDateTime now = DateUtil.getInstance().convertToLocalDateTime(new Date());

        return companyRepository.findById(id).flatMap(companyEntity -> {
            companyEntity.setUpdateTime(now);
            companyEntity.setStatus(Status.UPDATED);
            companyEntity.setDeleted(true);
            companyEntity.setDeletionTime(now);
            return companyRepository.save(companyEntity);
        })
                .switchIfEmpty(
                        Mono.error(new CompanyNotFoundException("id", id.toString())))
                .map(this::mapToCompanyDto);

    }

    protected CompanyDto mapToCompanyDto(CompanyEntity companyEntity) {
        return CompanyDto.builder()
                .id(companyEntity.getId())
                .name(companyEntity.getName())
                .address(companyEntity.getAddress())
                .phone(companyEntity.getPhone())
                .emailAddress(companyEntity.getEmailAddress())
                .websiteUrl(companyEntity.getWebsiteUrl())
                .build();
    }

    protected CompanyEntity mapToCompanyEntity(CompanyDto companyDto) {
        return CompanyEntity.builder()
                .id(companyDto.getId())
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .phone(companyDto.getPhone())
                .emailAddress(companyDto.getEmailAddress())
                .websiteUrl(companyDto.getWebsiteUrl())
                .build();
    }
}
