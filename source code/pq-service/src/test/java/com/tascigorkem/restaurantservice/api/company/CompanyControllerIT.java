package com.tascigorkem.restaurantservice.api.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tascigorkem.restaurantservice.api.response.Response;
import com.tascigorkem.restaurantservice.domain.DomainModelFaker;
import com.tascigorkem.restaurantservice.domain.company.CompanyDto;
import com.tascigorkem.restaurantservice.domain.company.CompanyPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
//R: check dirty context
class CompanyControllerIT {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @MockBean
    private CompanyPersistencePort companyPersistencePort;

    @Test
    void getCompany() {
        // arrange
        final WebTestClient client = WebTestClient.bindToApplicationContext(context).build();

        UUID fakeCompanyId = UUID.randomUUID();
        CompanyDto fakeCompanyDto = DomainModelFaker.getFakeCompanyDto(fakeCompanyId);
        when(companyPersistencePort.getCompanyById(fakeCompanyId)).thenReturn(Mono.just(fakeCompanyDto));

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
                            () -> assertEquals(fakeCompanyId, companyResponseDto.getId()),
                            () -> assertEquals(fakeCompanyDto.getName(), companyResponseDto.getName()),
                            () -> assertEquals(fakeCompanyDto.getAddress(), companyResponseDto.getAddress()),
                            () -> assertEquals(fakeCompanyDto.getPhone(), companyResponseDto.getPhone()),
                            () -> assertEquals(fakeCompanyDto.getEmailAddress(), companyResponseDto.getEmailAddress()),
                            () -> assertEquals(fakeCompanyDto.getWebsiteUrl(), companyResponseDto.getWebsiteUrl())
                    );
                });

    }
}
