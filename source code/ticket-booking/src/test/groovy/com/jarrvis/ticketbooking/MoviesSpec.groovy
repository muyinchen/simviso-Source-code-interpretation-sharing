package com.jarrvis.ticketbooking

import com.jarrvis.ticketbooking.ui.configuration.ApplicationConfig
import com.jarrvis.ticketbooking.ui.dto.request.AddNewMovieRequest
import com.jarrvis.ticketbooking.ui.dto.response.MovieResource
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
class MoviesSpec extends Specification {


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

        when: 'requesting movies api'
            def result = webTestClient
                    .get()
                    .uri("/movies")
                    .exchange()
        then:
            result
                    .expectStatus().isUnauthorized()
    }

    def "should be able to access api with authorization"() {

        when: 'requesting movies api'
            def result = webTestClient
                    .get()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .exchange()
        then:
            result
                    .expectStatus().isOk()
                    .expectBody().json("[]")
    }

    def "should be possible to save movie, having required authorization"() {
        given:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Joker")
                    .description("Joker")
                    .duration(120)
                    .build()

        when: 'saving movie'
            def result = webTestClient
                    .post()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewMovieRequest), AddNewMovieRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
    }

    def "should receive 422 status when accessing api with invalid request parameters"() {

        when: 'requesting movies api'
            def result = webTestClient
                    .post()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(AddNewMovieRequest.builder().name("").build()), AddNewMovieRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

    }

    def "should not be possible to save movie with the same name twice. should result in 409 status"() {
        given:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Batman")
                    .description("Batman")
                    .duration(120)
                    .build()

        when: 'saving movie'
            def result = webTestClient
                    .post()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewMovieRequest), AddNewMovieRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
        and: 'trying to save same movie again'
            def result2 = webTestClient
                    .post()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewMovieRequest), AddNewMovieRequest.class)
                    .exchange()
        then:
            result2
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)

    }

    def "should be possible to save movie and query it later"() {
        given:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Avengers")
                    .description("Avengers")
                    .duration(120)
                    .build()

        when: 'saving movie'
            def result = webTestClient
                    .post()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .body(Mono.just(addNewMovieRequest), AddNewMovieRequest.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
        and:
            def result2 = webTestClient
                    .get()
                    .uri("/movies")
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString((applicationConfig.webAccess.username + ":" + applicationConfig.webAccess.password).getBytes(UTF_8)))
                    .exchange()
        then:
            result2
                    .expectStatus().isOk()
                    .expectBodyList(MovieResource.class).consumeWith({ list -> assert list.getResponseBody().size() == 1 });

    }

}