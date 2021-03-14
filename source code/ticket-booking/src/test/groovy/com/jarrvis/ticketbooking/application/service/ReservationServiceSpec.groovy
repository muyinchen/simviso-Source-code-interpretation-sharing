package com.jarrvis.ticketbooking.application.service

import com.jarrvis.ticketbooking.domain.*
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

class ReservationServiceSpec extends Specification {

    private ReservationService reservationService
    private ReservationRepository reservationRepository
    private ScreeningRepository screeningRepository

    def setup() {
        reservationRepository = Mock(ReservationRepository)
        screeningRepository = Mock(ScreeningRepository)

        reservationService = new ReservationService(reservationRepository, screeningRepository)
    }

    def "Should not be possible to do reservation for non existing screening"() {
        given:
            screeningRepository.findById(_ as String) >> Mono.empty()

        when:
            reservationService.reserve("non existing", "Tony", "Stark", [ Ticket.of(Seat.of(1, 2), TicketType.ADULT) ] as Set)
                    .block()
        then:
            thrown(IllegalStateException)
    }

    def "Should book places and save screening"() {
        given:
            reservationRepository.save(_ as Reservation) >> { Reservation reservation -> Mono.just(reservation) }
            screeningRepository.save(_ as Screening) >> { Screening screening -> Mono.just(screening) }
            screeningRepository.findById(_ as String) >> Mono.just(
                    new Screening(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).minusHours(2), "Joker", "Dream", 10, 15))

        when:
            def result = reservationService.reserve("existing", "Tony", "Stark", [Ticket.of(Seat.of(1, 2), TicketType.ADULT) ] as Set)
                    .block()
        then:
            result
            //1 * screeningRepository.save(!null)
            //1 * reservationRepository.save(!null)
    }

    def "Should not be possible to confirm non existing reservation"() {
        given:
            reservationRepository.findByIdAndStatusAndToken(_ as String, _ as ReservationStatus, _ as String) >> Mono.empty()

        when:
            reservationService.confirm("non existing", "token")
                    .block()
        then:
            thrown(IllegalStateException)
    }

    def "Should be possible to confirm existing reservation"() {
        given:
            reservationRepository.findByIdAndStatusAndToken(_ as String, _ as ReservationStatus, _ as String) >> Mono.just(
                    new Reservation("id", "token", ReservationStatus.OPEN, LocalDateTime.now().plusHours(2), LocalDateTime.now(), "id", LocalDateTime.now().plusHours(2), "name", "surname", [] as Set, BigDecimal.TEN, Currency.PLN))


        when:
            reservationService.confirm("existing", "token")
                    .block()
        then:
            1 * reservationRepository.save(!null)
    }

    def "Should not be possible to cancel non existing reservation"() {
        given:
            reservationRepository.findByIdAndStatus(_ as String, _ as ReservationStatus) >> Mono.empty()

        when:
            reservationService.cancel("non existing").block()
        then:
            thrown(IllegalStateException)
    }

    def "Should not be possible to cancel reservation for non existing screening"() {
        given:
            reservationRepository.findByIdAndStatus(_ as String, _ as ReservationStatus) >> Mono.just(
                    new Reservation("id", "token", ReservationStatus.OPEN, LocalDateTime.now().plusHours(2), LocalDateTime.now(), "id", LocalDateTime.now().plusHours(2), "name", "surname", [] as Set, BigDecimal.TEN, Currency.PLN))
            screeningRepository.findById(_ as String) >> Mono.empty()

        when:
            reservationService.cancel("non existing").block()
        then:
            thrown(IllegalStateException)
    }

    def "Should cancel reservation and save screening and reservation"() {
        given:
            screeningRepository.findById(_ as String) >> Mono.just(
                    new Screening(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).minusHours(2), "Joker", "Dream", 10, 15))
            reservationRepository.findByIdAndStatus(_ as String, _ as ReservationStatus) >> Mono.just(
                    new Reservation("id", "token", ReservationStatus.OPEN, LocalDateTime.now().plusHours(2), LocalDateTime.now(), "id", LocalDateTime.now().plusHours(2), "name", "surname", [] as Set, BigDecimal.TEN, Currency.PLN))
            screeningRepository.save(_ as Screening) >> { Screening screening -> Mono.just(screening) }
            reservationRepository.save(_ as Reservation) >> { Reservation reservation -> Mono.just(reservation) }
        when:
            def result = reservationService.cancel("existing")
        then:
            result
            //1 * screeningRepository.save(!null)
            //1 * reservationRepository.save(!null)
    }
}