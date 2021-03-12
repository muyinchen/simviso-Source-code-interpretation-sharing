package com.tascigorkem.restaurantservice.api.company;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.company.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@RestController
public class CompanyControllerImpl implements CompanyController {

    private final CompanyService companyService;

    public CompanyControllerImpl(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public Mono<Response> getCompanies() {
        return companyService.getAllCompanies()
                .map(mapToCompanyControllerResponseDto())
                .collectList()
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> getCompanyById(UUID id) {
        return companyService.getCompanyById(id)
                .map(mapToCompanyControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> addCompany(CompanyControllerRequestDto companyControllerRequestDto) {
        return companyService.addCompany(mapToCompanyDto().apply(companyControllerRequestDto))
                .map(mapToCompanyControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> updateCompany(UUID id, CompanyControllerRequestDto companyControllerRequestDto) {
        CompanyDto companyDto = mapToCompanyDto().apply(companyControllerRequestDto);
        companyDto.setId(id);
        return companyService.updateCompany(companyDto)
                .map(mapToCompanyControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Mono<Response> removeCompany(UUID id) {
        return companyService.removeCompany(id)
                .map(mapToCompanyControllerResponseDto())
                .map(Response.ok()::setPayload)
                .cast(Response.class);
    }

    @Override
    public Function<CompanyDto, CompanyControllerResponseDto> mapToCompanyControllerResponseDto() {
        return companyDto ->
                CompanyControllerResponseDto.builder()
                        .id(companyDto.getId())
                        .name(companyDto.getName())
                        .address(companyDto.getAddress())
                        .phone(companyDto.getPhone())
                        .emailAddress(companyDto.getEmailAddress())
                        .websiteUrl(companyDto.getWebsiteUrl())
                        .build();
    }

    @Override
    public Function<CompanyControllerRequestDto, CompanyDto> mapToCompanyDto() {
        return companyControllerRequestDto ->
                CompanyDto.builder()
                        .name(companyControllerRequestDto.getName())
                        .address(companyControllerRequestDto.getAddress())
                        .phone(companyControllerRequestDto.getPhone())
                        .emailAddress(companyControllerRequestDto.getEmailAddress())
                        .websiteUrl(companyControllerRequestDto.getWebsiteUrl())
                        .build();
    }
}
