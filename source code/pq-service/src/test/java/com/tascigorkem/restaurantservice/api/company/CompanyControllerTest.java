package com.tascigorkem.restaurantservice.api.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.ApiModelFaker;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.company.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(CompanyController.class)
class CompanyControllerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @MockBean
    private CompanyService companyService;

    private CompanyController subject = new CompanyControllerImpl(companyService);

    /**
     * Unit test for CompanyController:getAllCompanies
     */
    @Test
    void testGetAllCompanies() {
        // arrange
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(DomainModelFaker.fakeId());
        List<CompanyDto> companyDtoList = Arrays.asList(fakeCompanyDto, fakeCompanyDto, fakeCompanyDto);
        when(companyService.getAllCompanies()).thenReturn(Flux.fromIterable(companyDtoList));

        // act
        client.get().uri("/companies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode())
                    );

                    List<CompanyControllerResponseDto> responseDtoList = Arrays.asList(
                            objectMapper.convertValue(response.getPayload(), CompanyControllerResponseDto[].class));

                    assertEquals(3, responseDtoList.size());

                    responseDtoList.forEach(responseDto ->
                            assertAll(
                                    () -> assertEquals(fakeCompanyDto.getId(), responseDto.getId()),
                                    () -> assertEquals(fakeCompanyDto.getName(), responseDto.getName()),
                                    () -> assertEquals(fakeCompanyDto.getAddress(), responseDto.getAddress()),
                                    () -> assertEquals(fakeCompanyDto.getPhone(), responseDto.getPhone()),
                                    () -> assertEquals(fakeCompanyDto.getEmailAddress(), responseDto.getEmailAddress()),
                                    () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), responseDto.getWebsiteUrl())
                            ));

                });
        verify(companyService).getAllCompanies();
    }

    /**
     * Unit test for CompanyController:getCompanyById
     */
    @Test
    void givenCompanyId_whenGetCompany_andCompanyExists_thenReturnCompany() {
        // arrange
        UUID fakeCompanyId = UUID.randomUUID();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyService.getCompanyById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyDto));
        // act
        client.get().uri("/companies/" + fakeCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                    CompanyControllerResponseDto companyResponseDto = objectMapper
                            .convertValue(response.getPayload(), CompanyControllerResponseDto.class);

                    assertAll(
                            () -> assertEquals(HttpStatus.OK, response.getStatus()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(fakeCompanyId, companyResponseDto.getId()),
                            () -> assertEquals(fakeCompanyDto.getName(), companyResponseDto.getName()),
                            () -> assertEquals(fakeCompanyDto.getAddress(), companyResponseDto.getAddress()),
                            () -> assertEquals(fakeCompanyDto.getPhone(), companyResponseDto.getPhone()),
                            () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyResponseDto.getEmailAddress()),
                            () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyResponseDto.getWebsiteUrl())
                    );
                });
        verify(companyService).getCompanyById(fakeCompanyId);
    }

    /**
     * Unit test for CompanyController:addCompany
     */
    @Test
    void givenCompanyControllerRequestDto_whenCreateCompany_thenReturnSuccessful_andReturnCompany() {
        // arrange
        CompanyControllerRequestDto fakeCompanyControllerRequestDto = ApiModelFaker.getCompanyControllerRequestDto();
        CompanyDto fakeCompanyDto = subject.mapToCompanyDto().apply(fakeCompanyControllerRequestDto);
        when(companyService.addCompany(fakeCompanyDto)).thenReturn(Mono.just(fakeCompanyDto));
        // act
        client.post().uri("/companies")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeCompanyControllerRequestDto), CompanyControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {
                            CompanyControllerResponseDto companyResponseDto = objectMapper
                                    .convertValue(response.getPayload(), CompanyControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeCompanyDto.getName(), companyResponseDto.getName()),
                                    () -> assertEquals(fakeCompanyDto.getAddress(), companyResponseDto.getAddress()),
                                    () -> assertEquals(fakeCompanyDto.getPhone(), companyResponseDto.getPhone()),
                                    () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyResponseDto.getEmailAddress()),
                                    () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyResponseDto.getWebsiteUrl())
                            );
                        }

                );
        verify(companyService).addCompany(fakeCompanyDto);
    }

    /**
     * Unit test for CompanyController:updateCompany
     */
    @Test
    void givenCompanyCompanyControllerRequestDto_andCompanyControllerRequestDto_whenUpdateCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = UUID.randomUUID();
        CompanyControllerRequestDto fakeCompanyControllerRequestDto = ApiModelFaker.getCompanyControllerRequestDto();
        CompanyDto fakeCompanyDto = subject.mapToCompanyDto().apply(fakeCompanyControllerRequestDto);
        fakeCompanyDto.setId(fakeCompanyId);
        when(companyService.updateCompany(fakeCompanyDto)).thenReturn(Mono.just(fakeCompanyDto));
        // act
        client.put().uri("/companies/" + fakeCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(fakeCompanyControllerRequestDto), CompanyControllerRequestDto.class)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                            CompanyControllerResponseDto companyResponseDto = objectMapper
                                    .convertValue(response.getPayload(), CompanyControllerResponseDto.class);
                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeCompanyDto.getName(), companyResponseDto.getName()),
                                    () -> assertEquals(fakeCompanyDto.getAddress(), companyResponseDto.getAddress()),
                                    () -> assertEquals(fakeCompanyDto.getPhone(), companyResponseDto.getPhone()),
                                    () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyResponseDto.getEmailAddress()),
                                    () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyResponseDto.getWebsiteUrl())
                            );
                        }

                );
        verify(companyService).updateCompany(fakeCompanyDto);
    }

    /**
     * Unit test for CompanyController:removeCompany
     */
    @Test
    void givenCompanyId_whenRemoveCompany_andExists_thenReturnSuccessful() {
        // arrange
        UUID fakeCompanyId = UUID.randomUUID();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyService.removeCompany(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyDto));
        // act
        client.delete().uri("/companies/" + fakeCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                // assert
                .expectStatus().isOk()
                .expectBody(Response.class)
                .value(response -> {

                            CompanyControllerResponseDto companyResponseDto = objectMapper
                                    .convertValue(response.getPayload(), CompanyControllerResponseDto.class);

                            assertAll(
                                    () -> assertEquals(HttpStatus.OK, response.getStatus()),
                                    () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                                    () -> assertEquals(fakeCompanyDto.getName(), companyResponseDto.getName()),
                                    () -> assertEquals(fakeCompanyDto.getAddress(), companyResponseDto.getAddress()),
                                    () -> assertEquals(fakeCompanyDto.getPhone(), companyResponseDto.getPhone()),
                                    () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyResponseDto.getEmailAddress()),
                                    () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyResponseDto.getWebsiteUrl())
                            );
                        }

                );
        verify(companyService).removeCompany(fakeCompanyId);
    }
}
