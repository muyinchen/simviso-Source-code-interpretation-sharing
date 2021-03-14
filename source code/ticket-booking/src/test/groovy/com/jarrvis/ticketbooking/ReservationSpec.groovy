package com.jarrvis.ticketbooking


import com.jarrvis.ticketbooking.domain.Screening
import com.jarrvis.ticketbooking.domain.ScreeningRepository
import com.jarrvis.ticketbooking.domain.ScreeningSeat
import com.jarrvis.ticketbooking.domain.Seat
import com.jarrvis.ticketbooking.domain.Ticket
import com.jarrvis.ticketbooking.domain.TicketType
import com.jarrvis.ticketbooking.ui.configuration.ApplicationConfig
import com.jarrvis.ticketbooking.ui.dto.request.ConfirmReservationCommand
import com.jarrvis.ticketbooking.ui.dto.request.CreateReservationCommand
import com.jarrvis.ticketbooking.ui.dto.response.ReservationResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationSpec extends Specification {


    @Autowired
    private WebTestClient webTestClient

    @Autowired
    private ApplicationConfig applicationConfig

    @Autowired
    private ScreeningRepository screeningRepository

    def setup() {

        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }


    def "should receive 422 status when accessing api with invalid request parameters"() {

        given:
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("")
                    .surname("")
                    .screeningId("not existing")
                    .tickets([Ticket.of(Seat.of(1, 1), TicketType.ADULT)] as Set)
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .post()
                    .uri("/reservations")
                    .body(Mono.just(createReservationCommand), CreateReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

    }

    def "should not be possible to create reservation, if screening does not exist"() {
        given:
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("Tony")
                    .surname("Stark")
                    .screeningId("not existing")
                    .tickets([Ticket.of(Seat.of(1, 1), TicketType.ADULT)] as Set)
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .post()
                    .uri("/reservations")
                    .body(Mono.just(createReservationCommand), CreateReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
    }

    def "should not be able to create reservation if screening starts in less than 15 minutes"() {
        given:
            Screening screening = this.screeningRepository.save(
                    new Screening(LocalDateTime.now().plusMinutes(14), LocalDateTime.now().plusDays(1).minusHours(2), "Joker", "Dream", 10, 15)).block()
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("Tony")
                    .surname("Stark")
                    .screeningId(screening.getId())
                    .tickets([Ticket.of(Seat.of(1, 1), TicketType.ADULT)] as Set)
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .post()
                    .uri("/reservations")
                    .body(Mono.just(createReservationCommand), CreateReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
    }

    def "should be able to create reservation"() {
        given:
            Screening screening = this.screeningRepository.save(
                    new Screening(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).minusHours(2), "Joker", "Dream", 10, 15)).block()
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("Tony")
                    .surname("Stark")
                    .screeningId(screening.getId())
                    .tickets([Ticket.of(Seat.of(1, 1), TicketType.ADULT)] as Set)
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .post()
                    .uri("/reservations")
                    .body(Mono.just(createReservationCommand), CreateReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isCreated()
    }

    def "should receive 422 status when trying to confirm reservation with invalid request parameters"() {

        given:
            def confirmReservationCommand = ConfirmReservationCommand.builder()
                    .reservationId("")
                    .token("")
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .patch()
                    .uri("/reservations")
                    .body(Mono.just(confirmReservationCommand), ConfirmReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

    }

    def "should not be able to confirm reservation if not found by id token and status#open"() {

        given:
            def confirmReservationCommand = ConfirmReservationCommand.builder()
                    .reservationId("not existing")
                    .token("not existing")
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .patch()
                    .uri("/reservations")
                    .body(Mono.just(confirmReservationCommand), ConfirmReservationCommand.class)
                    .exchange()
        then:
            result
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)

    }

    def "should be able to create and confirm reservation"() {
        given:
            Screening screening = this.screeningRepository.save(
                    new Screening(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).minusHours(2), "Joker", "Dream", 10, 15)).block()
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("Tony")
                    .surname("Stark")
                    .screeningId(screening.getId())
                    .tickets([Ticket.of(Seat.of(1, 1), TicketType.ADULT)] as Set)
                    .build()

        when: 'requesting reservation api'
            def result = webTestClient
                    .post()
                    .uri("/reservations")
                    .body(Mono.just(createReservationCommand), CreateReservationCommand.class)
                    .exchange()
        then:
            def responseBody = new ReservationResource()
            result
                    .expectStatus().isCreated()
                    .expectBody(ReservationResource.class)
                    .consumeWith({ res ->
                        responseBody = res.getResponseBody()
                    })
        and:
            def confirmReservationCommand = ConfirmReservationCommand.builder()
                    .reservationId(responseBody.reservationId)
                    .token(responseBody.token)
                    .build()

        when: 'requesting reservation api'
            def confirmResult = webTestClient
                    .patch()
                    .uri("/reservations")
                    .body(Mono.just(confirmReservationCommand), ConfirmReservationCommand.class)
                    .exchange()
        then:
            confirmResult
                    .expectStatus().isOk()

    }

}