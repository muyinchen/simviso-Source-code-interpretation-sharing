package com.jarrvis.ticketbooking

import com.jarrvis.ticketbooking.ui.configuration.ApplicationConfig
import com.jarrvis.ticketbooking.ui.dto.request.AddNewRoomRequest
import com.jarrvis.ticketbooking.ui.dto.response.RoomResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.Base64Utils
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

import static java.nio.charset.StandardCharsets.UTF_8

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomsSpec extends Specification {


    @Autowired
    private WebTestClient webTestClient

    @Autowired
    private ApplicationConfig applicationConfig


    def setup() {

        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    def "should not be able to access api without authorization"() {

        when: 'requesting rooms api'
            def result = webTestClient
                    .get()
                    .uri("/rooms")
                    .exchange()
        then:
            result
                    .expectStatus().isUnauthorized()
    }

    def "should be able to access api with authorization"() {

        when: 'requesting rooms api'
            def result = webTestClient
                    .get()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .exchange()
        then:
            result
                    .expectStatus().isOk()
                    .expectBody().json("[]")
    }

    def "should be possible to save room, having required authorization"() {
        given:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Dream")
                    .rows(10)
                    .seatsPerRow(15)
                    .build()
        when: 'saving movie'
            def result = webTestClient
                    .post()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewRoomRequest), AddNewRoomRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
    }

    def "should receive 422 status when accessing api with invalid request parameters"() {

        when: 'requesting rooms api'
            def result = webTestClient
                    .post()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(AddNewRoomRequest.builder().name("").build()), AddNewRoomRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

    }

    def "should not be possible to save room with the same name twice. should result in 409 status"() {
        given:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Dream")
                    .rows(10)
                    .seatsPerRow(15)
                    .build()
        when: 'saving room'
            def result = webTestClient
                    .post()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewRoomRequest), AddNewRoomRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()

        and: 'trying to save same room again'
            def result2 = webTestClient
                    .post()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewRoomRequest), AddNewRoomRequest.class)
                    .exchange()
        then:
            result2
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)

    }

    def "should be possible to save room and query it later"() {
        given:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Dream")
                    .rows(10)
                    .seatsPerRow(15)
                    .build()
        when: 'saving movie'
            def result = webTestClient
                    .post()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewRoomRequest), AddNewRoomRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
        and:
            def result2 = webTestClient
                    .get()
                    .uri("/rooms")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .exchange()
        then:
            result2
                    .expectStatus().isOk()
                    .expectBodyList(RoomResource.class).consumeWith({ list -> assert list.getResponseBody().size() == 1 });

    }
}