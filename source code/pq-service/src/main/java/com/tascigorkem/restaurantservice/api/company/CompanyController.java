package com.tascigorkem.restaurantservice.api.company;

import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

public interface CompanyController {

    /**
     * Handles the incoming GET request "/companies"
     *
     * @return retrieve all non-deleted companies
     *
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @GetMapping("/companies")
    Mono<Response> getCompanies();

    /**
     * Handles the incoming GET request "/companies/{id}"
     *
     * @param id of the company to be retrieved
     * @return company
     *
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @GetMapping("/companies/{id}")
    Mono<Response> getCompanyById(@PathVariable("id") UUID id);

    /**
     * Handles the incoming POST request "/companies"
     *
     * @param companyControllerRequestDto fields of company to be added
     * @return added company
     *
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @PostMapping("/companies")
    Mono<Response> addCompany(@RequestBody CompanyControllerRequestDto companyControllerRequestDto);

    /**
     * Handles the incoming PUT request "/companies/{id}"
     *
     * @param id of the company to be updated
     * @param companyControllerRequestDto fields of company to be updated
     * @return updated company
     *
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @PutMapping("/companies/{id}")
    Mono<Response> updateCompany(@PathVariable("id") UUID id, @RequestBody CompanyControllerRequestDto companyControllerRequestDto);

    /**
     * Handles the incoming DELETE request "/companies/{id}"
     *
     * @param id of the company to be deleted
     * @return removed company
     *
     * @see com.tascigorkem.restaurantservice.api.company.CompanyControllerResponseDto
     */
    @DeleteMapping("/companies/{id}")
    Mono<Response> removeCompany(@PathVariable("id") UUID id);

    Function<CompanyDto, CompanyControllerResponseDto> mapToCompanyControllerResponseDto();

    Function<CompanyControllerRequestDto, CompanyDto> mapToCompanyDto();
}
